package cpu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import cpu.CPU.State;
import ram.Ram;

/**
 * Instruction set of the CPU, containing all instructions connected to their
 * opcodes.
 */
public class InstructrionSet
{

	interface Instruction
	{
		public void invoke();
	}

	private interface Condition
	{
		public boolean evaluate();
	}

	private interface AccumulatorInstructions
	{
		public void invoke(byte value);
	}

	private interface RegisterOperation
	{
		public void invoke(Register8Bit register);
	}

	private class RegisterPair
	{
		final Register8Bit first;
		final Register8Bit second;

		public RegisterPair(Register8Bit first, Register8Bit second)
		{
			this.first = first;
			this.second = second;
		}
	}

	protected interface TickRequiertFunction
	{
		public void invokeOperation();
	}

	private Queue<TickRequiertFunction> operationQueue;
	private Map<String, Instruction> instructions;
	private Map<String, Instruction> instructionsCB;

	private byte lowShadow;
	private byte highShadow;

	public InstructrionSet(CPU cpu, Ram ram, Accumulator accu, ProgrammCounter pc, Register16bit sp, Register8Bit b,
			Register8Bit c, Register8Bit d, Register8Bit e, Register8Bit h, Register8Bit l, Flags flags)
	{
		this.operationQueue = new LinkedList<>();
		lowShadow = 0;
		highShadow = 0;

		HashMap<Character, Register8Bit> opCodeToRegister = new HashMap<>();
		opCodeToRegister.put('0', b);
		opCodeToRegister.put('1', c);
		opCodeToRegister.put('2', d);
		opCodeToRegister.put('3', e);
		opCodeToRegister.put('4', h);
		opCodeToRegister.put('5', l);
		opCodeToRegister.put('7', accu);

		HashMap<Character, RegisterPair> opCodeToRegisterPairLow = new HashMap<>();
		HashMap<Character, RegisterPair> opCodeToRegisterPairHigh = new HashMap<>();
		opCodeToRegisterPairLow.put('0', new RegisterPair(b, c));
		opCodeToRegisterPairHigh.put('1', new RegisterPair(b, c));
		opCodeToRegisterPairLow.put('2', new RegisterPair(d, e));
		opCodeToRegisterPairHigh.put('3', new RegisterPair(d, e));
		opCodeToRegisterPairLow.put('4', new RegisterPair(h, l));
		opCodeToRegisterPairHigh.put('5', new RegisterPair(h, l));

		HashMap<Character, AccumulatorInstructions> opCodeToOperation = new HashMap<>();
		opCodeToOperation.put('0', (byte value) -> accu.add(value));
		opCodeToOperation.put('1', (byte value) -> accu.adC(value));
		opCodeToOperation.put('2', (byte value) -> accu.sub(value));
		opCodeToOperation.put('3', (byte value) -> accu.sbC(value));
		opCodeToOperation.put('4', (byte value) -> accu.and(value));
		opCodeToOperation.put('5', (byte value) -> accu.xOr(value));
		opCodeToOperation.put('6', (byte value) -> accu.or(value));
		opCodeToOperation.put('7', (byte value) -> accu.cp(value));

		HashMap<Character, RegisterOperation> cbOpCodeToOperation = new HashMap<>();
		cbOpCodeToOperation.put('0', (Register8Bit register) -> register.rlc());
		cbOpCodeToOperation.put('1', (Register8Bit register) -> register.rrc());
		cbOpCodeToOperation.put('2', (Register8Bit register) -> register.rl());
		cbOpCodeToOperation.put('3', (Register8Bit register) -> register.rr());
		cbOpCodeToOperation.put('4', (Register8Bit register) -> register.sla());
		cbOpCodeToOperation.put('5', (Register8Bit register) -> register.sra());
		cbOpCodeToOperation.put('6', (Register8Bit register) -> register.swap());
		cbOpCodeToOperation.put('7', (Register8Bit register) -> register.srl());

		HashMap<Character, Condition> opCodeToCondition = new HashMap<>();
		opCodeToCondition.put('0', () -> {
			return !flags.isZeroFlag();
		});
		opCodeToCondition.put('1', () -> {
			return flags.isZeroFlag();
		});
		opCodeToCondition.put('2', () -> {
			return !flags.isCarryFlag();
		});
		opCodeToCondition.put('3', () -> {
			return flags.isCarryFlag();
		});

		HashMap<Character, Byte> opCodeToPosition = new HashMap<>();
		opCodeToPosition.put('0', (byte) 0);
		opCodeToPosition.put('1', (byte) 1);
		opCodeToPosition.put('2', (byte) 2);
		opCodeToPosition.put('3', (byte) 3);
		opCodeToPosition.put('4', (byte) 4);
		opCodeToPosition.put('5', (byte) 5);
		opCodeToPosition.put('6', (byte) 6);
		opCodeToPosition.put('7', (byte) 7);

		instructions = new HashMap<>();

		for (Entry<Character, RegisterPair> from : opCodeToRegisterPairLow.entrySet())
		{
			instructions.put("0" + from.getKey() + "1",
					() -> ld_R1R2_d16(pc, ram, from.getValue().first, from.getValue().second));
			instructions.put("0" + from.getKey() + "2",
					() -> ld_aR1R2_R3(ram, from.getValue().first, from.getValue().second, accu));
			instructions.put("0" + from.getKey() + "3",
					() -> inc_R1R2(flags, from.getValue().first, from.getValue().second));

			instructions.put("3" + from.getKey() + "1",
					() -> popNN(sp, ram, from.getValue().first, from.getValue().second));
			instructions.put("3" + from.getKey() + "5",
					() -> pushNN(sp, ram, from.getValue().first, from.getValue().second));
		}

		for (Entry<Character, RegisterPair> from : opCodeToRegisterPairHigh.entrySet())
		{
			instructions.put("0" + from.getKey() + "1",
					() -> add_HL_R1R2(flags, h, l, from.getValue().first, from.getValue().second));
			instructions.put("0" + from.getKey() + "2",
					() -> ld_R1_aR2R3(ram, from.getValue().first, from.getValue().second, accu));
			instructions.put("0" + from.getKey() + "3",
					() -> dec_R1R2(flags, from.getValue().first, from.getValue().second));
		}

		instructions.put("000", () -> {
		});
		instructions.put("010", () -> ld_a16_SP(pc, sp, flags, ram));
		instructions.put("020", () -> cpu.setState(State.STOP));
		instructions.put("030", () -> jrCCN(pc, ram, true));
		instructions.put("040", () -> jrCCN(pc, ram, !flags.isZeroFlag()));
		instructions.put("050", () -> jrCCN(pc, ram, flags.isZeroFlag()));
		instructions.put("060", () -> jrCCN(pc, ram, !flags.isCarryFlag()));
		instructions.put("070", () -> jrCCN(pc, ram, flags.isCarryFlag()));

		instructions.put("007", () -> accu.rlca());
		instructions.put("017", () -> accu.rrca());
		instructions.put("027", () -> accu.rla());
		instructions.put("037", () -> accu.rra());
		instructions.put("047", () -> accu.da());
		instructions.put("057", () -> accu.cpl());
		instructions.put("067", () -> flags.scf());
		instructions.put("077", () -> flags.ccf());

		instructions.put("042", () -> ld_aHLInc_A(ram, accu, h, l, flags));

		instructions.put("052", () -> ld_A_aHLInc(ram, accu, h, l, flags));

		instructions.put("062", () -> ld_aHLDec_A(ram, accu, h, l, flags));

		instructions.put("072", () -> ld_A_aHLDec(ram, accu, h, l, flags));

		instructions.put("061", () -> ld_R_d16(pc, ram, sp));
		instructions.put("071", () -> add_HL_R(flags, ram, h, l, sp));

		instructions.put("063", () -> inc_R(sp));
		instructions.put("073", () -> dec_R(sp));

		instructions.put("064", () -> inc_aHL(flags, ram, h, l));
		instructions.put("065", () -> dec_aHL(flags, ram, h, l));
		instructions.put("066", () -> ld_aHL_d8(pc, ram, h, l));

		for (Map.Entry<Character, Register8Bit> from : opCodeToRegister.entrySet())
		{
			instructions.put("0" + from.getKey() + "4", () -> from.getValue().inc());
			instructions.put("0" + from.getKey() + "5", () -> from.getValue().dcr());
			instructions.put("0" + from.getKey() + "6", () -> ld_R1_d8(pc, ram, from.getValue()));

			for (Map.Entry<Character, Register8Bit> into : opCodeToRegister.entrySet())
			{
				instructions.put("1" + into.getKey() + from.getKey(),
						() -> into.getValue().put(from.getValue().load()));
			}

			instructions.put("1" + from.getKey() + "6", () -> this.ld_R1_aR2R3(ram, h, l, from.getValue()));

			instructions.put("1" + "6" + from.getKey(), () -> this.ld_aR1R2_R3(ram, h, l, from.getValue()));
		}

		instructions.put("166", () -> cpu.setState(State.HALT));

		for (Map.Entry<Character, AccumulatorInstructions> operation : opCodeToOperation.entrySet())
		{
			for (Map.Entry<Character, Register8Bit> with : opCodeToRegister.entrySet())
			{
				instructions.put("2" + operation.getKey() + with.getKey(),
						() -> operation.getValue().invoke(with.getValue().load()));
			}

			instructions.put("2" + operation.getKey() + "6",
					() -> operationQueue.add(() -> operation.getValue().invoke(ram.load(h.load(), l.load()))));

			instructions.put("3" + operation.getKey() + "6", () -> {
				operationQueue.add(() -> {
					operation.getValue().invoke(ram.load(pc.load()));
				});
			});
		}

		for (byte opKey = 0; opKey < 8; opKey++)
		{
			final byte adress = (byte) (opKey * 8);
			instructions.put("3" + opKey + "7", () -> rst(pc, sp, ram, adress));
		}

		for (Entry<Character, Condition> condition : opCodeToCondition.entrySet())
		{
			instructions.put("3" + condition.getKey() + "0", () -> retCC(pc, sp, ram, condition.getValue().evaluate()));
			instructions.put("3" + condition.getKey() + "2", () -> jpCCNN(pc, ram, condition.getValue().evaluate()));
			instructions.put("3" + condition.getKey() + "4",
					() -> callCCNN(pc, sp, ram, condition.getValue().evaluate()));

		}

		instructions.put("303", () -> jpCCNN(pc, ram, true));
		instructions.put("311", () -> ret(pc, sp, ram));
		instructions.put("313", () -> this.prefixCB(pc, ram, cpu));
		instructions.put("315", () -> callCCNN(pc, sp, ram, true));
		instructions.put("331", () -> reti(cpu, ram, pc, sp));

		instructions.put("340", () -> ldh_a8_A(pc, ram, accu));
		instructions.put("342", () -> ld_aR1R2_R3(ram, new Register8Bit((byte) 255, flags), c, accu));

		instructions.put("350", () -> add_SP_r8(pc, ram, sp));
		instructions.put("351", () -> pc.put(h.load(), l.load()));
		instructions.put("352", () -> ld_a16_A(pc, ram, accu));

		instructions.put("360", () -> ldh_A_a8(pc, ram, accu));
		instructions.put("361", () -> popNN(sp, ram, accu, flags));
		instructions.put("362", () -> ld_R1_aR2R3(ram, new Register8Bit((byte) 255, flags), c, accu));
		instructions.put("363", () -> cpu.setInterruptMasterEnableImediate(false));
		instructions.put("365", () -> pushNN(sp, ram, accu, flags));

		instructions.put("370", () -> ld_HL_SPr8(pc, flags, ram, h, l, sp));
		instructions.put("371", () -> ld_SP_HL(h, l, sp));
		instructions.put("372", () -> ld_A_a16(pc, ram, accu));
		instructions.put("373", () -> cpu.setInterruptMasterEnableDelayed(true));

		instructionsCB = new HashMap<>();

		for (Entry<Character, RegisterOperation> operation : cbOpCodeToOperation.entrySet())
		{
			for (Map.Entry<Character, Register8Bit> with : opCodeToRegister.entrySet())
			{
				instructionsCB.put("0" + operation.getKey() + with.getKey(),
						() -> operation.getValue().invoke(with.getValue()));
			}

			instructionsCB.put("0" + operation.getKey() + "6", () -> {
				operationQueue.add(() -> {
					lowShadow = ram.load(h.load(), l.load());
				});

				operationQueue.add(() -> {
					Register8Bit shedow = new Register8Bit(lowShadow, flags);
					operation.getValue().invoke(shedow);
					ram.put(h.load(), l.load(), shedow.load());
				});
			});

		}

		for (Entry<Character, Byte> position : opCodeToPosition.entrySet())
		{
			for (Map.Entry<Character, Register8Bit> with : opCodeToRegister.entrySet())
			{
				instructionsCB.put("1" + position.getKey() + with.getKey(),
						() -> with.getValue().bit(position.getValue()));
				instructionsCB.put("2" + position.getKey() + with.getKey(),
						() -> with.getValue().res(position.getValue()));
				instructionsCB.put("3" + position.getKey() + with.getKey(),
						() -> with.getValue().set(position.getValue()));
			}

			instructionsCB.put("1" + position.getKey() + "6", () -> {
				operationQueue.add(() -> {
					lowShadow = ram.load(h.load(), l.load());
					Register8Bit shedow = new Register8Bit(lowShadow, flags);
					shedow.bit(position.getValue());
				});
			});
			instructionsCB.put("2" + position.getKey() + "6", () -> {
				operationQueue.add(() -> {
					lowShadow = ram.load(h.load(), l.load());
				});
				operationQueue.add(() -> {
					Register8Bit shedow = new Register8Bit(lowShadow, flags);
					shedow.res(position.getValue());
					ram.put(h.load(), l.load(), shedow.load());
				});
			});
			instructionsCB.put("3" + position.getKey() + "6", () -> {
				operationQueue.add(() -> {
					lowShadow = ram.load(h.load(), l.load());
				});
				operationQueue.add(() -> {
					Register8Bit shedow = new Register8Bit(lowShadow, flags);
					shedow.set(position.getValue());
					ram.put(h.load(), l.load(), shedow.load());
				});
			});
		}
	}

	/**
	 * Returns and enables interrupts afterward.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @Cycle3: Interrupt master enable
	 */
	private void reti(CPU cpu, Ram ram, ProgrammCounter pc, Register16bit sp)
	{
		operationQueue.add(() -> {
			pc.put(pc.loadHigh(), ram.load(sp.loadHigh(), sp.loadLow()));
			sp.inc();
		});

		operationQueue.add(() -> {
			pc.put(ram.load(sp.loadHigh(), sp.loadLow()), pc.loadLow());
			sp.inc();
		});

		operationQueue.add(() -> {
			cpu.setInterruptMasterEnableImediate(true);
		});
	}

	/**
	 * Loads the Ram that is addressed by the given registers to the
	 * accumulator. Decrements the address registers afterward.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access and decrement of registers
	 */
	private void ld_A_aHLDec(Ram ram, Accumulator accu, Register8Bit h, Register8Bit l, Flags flags)
	{
		operationQueue.add(() -> {
			accu.put(ram.load(h.load(), l.load()));
			decrementHL(h, l, flags);
		});
	}

	/**
	 * Puts the Accumulator to Ram that is addressed by the given registers.
	 * Decrements the address registers afterward.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n write, Memory access and decrement of registers
	 */
	private void ld_aHLDec_A(Ram ram, Accumulator accu, Register8Bit h, Register8Bit l, Flags flags)
	{
		operationQueue.add(() -> {
			ram.put(h.load(), l.load(), accu.load());
			decrementHL(h, l, flags);
		});
	}

	/**
	 * Loads the Ram that is addressed by the given registers to the
	 * accumulator. Increment the address registers afterward.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access and increment of registers
	 */
	private void ld_A_aHLInc(Ram ram, Accumulator accu, Register8Bit h, Register8Bit l, Flags flags)
	{
		operationQueue.add(() -> {
			accu.put(ram.load(h.load(), l.load()));
			incrementHL(h, l, flags);
		});
	}

	/**
	 * Puts the Accumulator to Ram that is addressed by the given registers.
	 * Increment the address registers afterward.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n write, Memory access and increment of registers
	 */
	private void ld_aHLInc_A(Ram ram, Accumulator accu, Register8Bit h, Register8Bit l, Flags flags)
	{
		operationQueue.add(() -> {
			ram.put(h.load(), l.load(), accu.load());
			incrementHL(h, l, flags);
		});
	}

	/**
	 * Executes the next instruction in RAM as it is a CB instruction.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access for the opcode and invocation of the
	 *          fitting cb instruction.
	 */
	private void prefixCB(ProgrammCounter pc, Ram ram, CPU cpu)
	{
		operationQueue.add(() -> {
			instructionsCB.get(cpu.loadOctalOpCode(pc, ram)).invoke();
		});
	}

	/**
	 * Restart for software interrupts, acts like a fast call to special
	 * addresses.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Internal delay
	 * @Cycle2: Pc push, Memory access for high byte
	 * @Cycle3: Pc push, Memory access for low byte
	 */
	public void rst(ProgrammCounter pc, Register16bit sp, Ram ram, byte adress)
	{
		operationQueue.add(() -> {
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), pc.loadHigh());
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), pc.loadLow());
			pc.put(adress);
		});

	}

	/**
	 * Restart for hardware interrupts, acts like a fast call to special
	 * addresses.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Internal delay
	 * @Cycle2: Internal delay
	 * @Cycle3: Internal delay
	 * @Cycle4: Pc push, Memory access for high byte and saving of jump address
	 * @Cycle5: Pc push, Memory access for low byte and put jump address into pc
	 */
	public void rst(ProgrammCounter pc, Register16bit sp, Ram ram, InterruptFlagRegister adress,
			InterruptEnableRegister interruptEnable)
	{
		operationQueue.add(() -> {
		});

		operationQueue.add(() -> {
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), pc.loadHigh());
			lowShadow = adress.getCurrentInterruptJumpAdress(interruptEnable);
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), pc.loadLow());
			pc.put(lowShadow);
		});
	}

	/**
	 * Returns to last address on the stack if condition is true
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Internal delay
	 * @End: if condition is false, else continue
	 * @Cycle2: Pc pop, Memory access for low byte
	 * @Cycle3: Pc pop, Memory access for high byte
	 * @Cycle4: Internal delay
	 */
	private void retCC(ProgrammCounter pc, Register16bit sp, Ram ram, boolean condition)
	{
		operationQueue.add(() -> {
		});

		if (condition)
		{
			operationQueue.add(() -> {
				pc.put(pc.loadHigh(), ram.load(sp.loadHigh(), sp.loadLow()));
				sp.inc();
			});

			operationQueue.add(() -> {
				pc.put(ram.load(sp.loadHigh(), sp.loadLow()), pc.loadLow());
				sp.inc();
			});

			operationQueue.add(() -> {
			});
		}
	}

	/**
	 * Returns to last address on the stack
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Pc pop, Memory access for low byte
	 * @Cycle2: Pc pop, Memory access for high byte
	 * @Cycle3: Internal delay
	 */
	private void ret(ProgrammCounter pc, Register16bit sp, Ram ram)
	{
		operationQueue.add(() -> {
			pc.put(pc.loadHigh(), ram.load(sp.loadHigh(), sp.loadLow()));
			sp.inc();
		});

		operationQueue.add(() -> {
			pc.put(ram.load(sp.loadHigh(), sp.loadLow()), pc.loadLow());
			sp.inc();
		});

		operationQueue.add(() -> {
		});
	}

	/**
	 * Pops two values from stack to registers.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: low pop, Memory access for low byte
	 * @Cycle2: high pop, Memory access for high byte
	 */
	private void popNN(Register16bit sp, Ram ram, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			low.put(ram.load(sp.loadHigh(), sp.loadLow()));
			sp.inc();
		});

		operationQueue.add(() -> {
			high.put(ram.load(sp.loadHigh(), sp.loadLow()));
			sp.inc();
		});
	}

	/**
	 * Pushes two values from registers to the stack
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Internal delay
	 * @Cycle2: high push, Memory access for high byte
	 * @Cycle3: low push, Memory access for low byte
	 */
	private void pushNN(Register16bit sp, Ram ram, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), high.load());
		});

		operationQueue.add(() -> {
			sp.dcr();
			ram.put(sp.loadHigh(), sp.loadLow(), low.load());
		});

	}

	/**
	 * Calls an address if condition is true
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @End: if condition is false, else continue
	 * @Cycle3: Internal delay
	 * @Cycle4: Pc push, Memory access for low byte
	 * @Cycle5: Pc push, Memory access for high byte and new address to pc
	 */
	private void callCCNN(ProgrammCounter pc, Register16bit sp, Ram ram, boolean condition)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			highShadow = ram.load(pc.load());
		});

		if (condition)
		{
			operationQueue.add(() -> {
			});

			operationQueue.add(() -> {
				sp.dcr();
				ram.put(sp.loadHigh(), sp.loadLow(), pc.loadHigh());
			});

			operationQueue.add(() -> {
				sp.dcr();
				ram.put(sp.loadHigh(), sp.loadLow(), pc.loadLow());
				pc.put(highShadow, lowShadow);
			});
		}

	}

	/**
	 * If condition is true, adds the value as an signed value to the pc
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: Internal delay and adding to pc
	 */
	private void jrCCN(ProgrammCounter pc, Ram ram, boolean condition)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		if (condition)
		{
			operationQueue.add(() -> {
				pc.addSignedNoFlags(lowShadow);
			});
		}

	}

	/**
	 * If condition is true, jumps to the loaded address
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @End: if condition is false, else continue
	 * @Cycle3: Internal delay and putting loadet adress to pc
	 */
	private void jpCCNN(ProgrammCounter pc, Ram ram, boolean condition)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});
		operationQueue.add(() -> {
			highShadow = ram.load(pc.load());
		});

		if (condition)
		{
			operationQueue.add(() -> {
				pc.put(highShadow, lowShadow);
			});
		}
	}

	/**
	 * Puts the stack pointer to RAM addressed by a immediate 16 bit value.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @Cycle3: SP write, Memory access for low byte
	 * @Cycle4: SP write, Memory access for high byte
	 */
	private void ld_a16_SP(ProgrammCounter pc, Register16bit sp, Flags flags, Ram ram)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			highShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put(highShadow, lowShadow, sp.loadLow());
		});

		operationQueue.add(() -> {
			Register16bit pointer = new Register16bit(highShadow, lowShadow, flags);
			pointer.inc();
			ram.put(pointer.loadHigh(), pointer.loadLow(), sp.loadHigh());
		});
	}

	/**
	 * Puts h and l together in the stack pointer register.
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Loads h and l to the stack pointer
	 */
	private void ld_SP_HL(Register8Bit h, Register8Bit l, Register16bit sp)
	{
		operationQueue.add(() -> {
			sp.put(h.load(), l.load());
		});
	}

	/**
	 * Loads a value from RAM addressed by a register pair to a register
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 */
	private void ld_R1_aR2R3(Ram ram, Register8Bit high, Register8Bit low, Register8Bit to)
	{
		operationQueue.add(() -> {
			to.put(ram.load(high.load(), low.load()));
		});
	}

	/**
	 * Puts a value from a register to RAM addressed by a register pair
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n write, Memory access
	 */
	private void ld_aR1R2_R3(Ram ram, Register8Bit high, Register8Bit low, Register8Bit from)
	{
		operationQueue.add(() -> {
			ram.put(high.load(), low.load(), from.load());
		});
	}

	/**
	 * Loads a value from RAM, addressed by a immediate 16 bit value, to the
	 * accumulator
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @Cycle3: n read, Memory access
	 */
	private void ld_A_a16(ProgrammCounter pc, Ram ram, Register8Bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			highShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			register.put(ram.load(highShadow, lowShadow));
		});
	}

	/**
	 * Puts the accumulator into RAM, addressed by a immediate 16 bit value,
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access for low byte
	 * @Cycle2: nn read, Memory access for high byte
	 * @Cycle3: n write, Memory access
	 */
	private void ld_a16_A(ProgrammCounter pc, Ram ram, Register8Bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			highShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put(highShadow, lowShadow, register.load());
		});

	}

	/**
	 * Loads high RAM addressed by a immediate 8 bit value to the accumulator
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access for low byte
	 * @Cycle3: n read, Memory access
	 */
	private void ldh_A_a8(ProgrammCounter pc, Ram ram, Register8Bit to)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			to.put(ram.load((byte) 255, lowShadow));
		});
	}

	/**
	 * Puts accumulator to high RAM addressed by a immediate 8 bit value
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access for low byte
	 * @Cycle3: n write, Memory access
	 */
	private void ldh_a8_A(ProgrammCounter pc, Ram ram, Register8Bit from)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put((byte) 255, lowShadow, from.load());
		});
	}

	/**
	 * Loads a immediate 8 bit value into a register
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 */
	private void ld_R1_d8(ProgrammCounter pc, Ram ram, Register8Bit register)
	{
		operationQueue.add(() -> {
			register.put(ram.load(pc.load()));
		});
	}

	/**
	 * Loads a immediate 8 bit value into RAM addressed by hl
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: n write, Memory access
	 */
	private void ld_aHL_d8(ProgrammCounter pc, Ram ram, Register8Bit h, Register8Bit l)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put(h.load(), l.load(), lowShadow);
		});
	}

	/**
	 * Increments a register pair
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Increment of register pair
	 */
	private void inc_R1R2(Flags flags, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit toIncrement = new Register16bit(high.load(), low.load(), flags);

			toIncrement.inc();

			high.put(toIncrement.loadHigh());
			low.put(toIncrement.loadLow());
		});
	}

	/**
	 * Increments a 16 bit register
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Increment of 16 bit register
	 */
	private void inc_R(Register16bit register)
	{
		operationQueue.add(() -> {
			register.inc();
		});
	}

	/**
	 * Decrement a register pair
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Decrement of register pair
	 */
	private void dec_R1R2(Flags flags, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit toIncrement = new Register16bit(high.load(), low.load(), flags);

			toIncrement.dcr();

			high.put(toIncrement.loadHigh());
			low.put(toIncrement.loadLow());
		});
	}

	/**
	 * Decrement a 16 bit register
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Decrement of 16 bit register
	 */
	private void dec_R(Register16bit register)
	{
		operationQueue.add(() -> {
			register.dcr();
		});
	}

	/**
	 * Increment RAM addressed by HL
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: n write, Memory access
	 */
	private void inc_aHL(Flags flags, Ram ram, Register8Bit h, Register8Bit l)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(h.load(), l.load());
		});

		operationQueue.add(() -> {
			Register8Bit loadetValue = new Register8Bit(lowShadow, flags);
			loadetValue.inc();
			ram.put(h.load(), l.load(), loadetValue.load());
		});
	}

	/**
	 * Decrement RAM addressed by HL
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: n write, Memory access
	 */
	private void dec_aHL(Flags flags, Ram ram, Register8Bit h, Register8Bit l)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(h.load(), l.load());
		});

		operationQueue.add(() -> {
			Register8Bit loadetValue = new Register8Bit(lowShadow, flags);
			loadetValue.dcr();
			ram.put(h.load(), l.load(), loadetValue.load());
		});
	}

	/**
	 * Add register pair value to HL
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Add register pair to HL
	 */
	private void add_HL_R1R2(Flags flags, Register8Bit h, Register8Bit l, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit hl = new Register16bit(h.load(), l.load(), flags);
			hl.add(high.load(), low.load());

			h.put(hl.loadHigh());
			l.put(hl.loadLow());
		});
	}

	/**
	 * Add 16 bit register value to HL
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: Add 16 bit register to HL
	 */
	private void add_HL_R(Flags flags, Ram ram, Register8Bit h, Register8Bit l, Register16bit register)
	{
		operationQueue.add(() -> {
			Register16bit hl = new Register16bit(h.load(), l.load(), flags);
			hl.add(register.load());

			h.put(hl.loadHigh());
			l.put(hl.loadLow());
		});
	}

	/**
	 * Put value from SP plus a signed immediate value to HL
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: Put value from SP plus a signed immediate value to HL
	 */
	private void ld_HL_SPr8(ProgrammCounter pc, Flags flags, Ram ram, Register8Bit h, Register8Bit l,
			Register16bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			Register16bit hl = new Register16bit(register.load(), flags);
			hl.addSigned(lowShadow);
			h.put(hl.loadHigh());
			l.put(hl.loadLow());
		});
	}

	/**
	 * Adds a signed immediate value to SP
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: n read, Memory access
	 * @Cycle2: Add the signed immediate value to SP
	 * @Cycle3: Internal delay
	 */
	private void add_SP_r8(ProgrammCounter pc, Ram ram, Register16bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});
		operationQueue.add(() -> {
			register.addSigned(lowShadow);
		});
		operationQueue.add(() -> {
		});
	}

	/**
	 * Puts a immediate 16 bit value in a register pair
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access low byte
	 * @Cycle2: nn read, Memory access high byte
	 */
	private void ld_R1R2_d16(ProgrammCounter pc, Ram ram, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			low.put(ram.load(pc.load()));
		});

		operationQueue.add(() -> {
			high.put(ram.load(pc.load()));
		});
	}

	/**
	 * Puts a immediate 16 bit value in a 16 bit register
	 * 
	 * @Cycle0: Instruction decoding
	 * @Cycle1: nn read, Memory access low byte
	 * @Cycle2: nn read, Memory access high byte
	 */
	private void ld_R_d16(ProgrammCounter pc, Ram ram, Register16bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			register.put(ram.load(pc.load()), lowShadow);
		});

	}

	private void decrementHL(Register8Bit h, Register8Bit l, Flags flags)
	{
		Register16bit hl = new Register16bit(h.load(), l.load(), flags);
		hl.dcr();
		h.put(hl.loadHigh());
		l.put(hl.loadLow());
	}

	private void incrementHL(Register8Bit h, Register8Bit l, Flags flags)
	{
		Register16bit hl = new Register16bit(h.load(), l.load(), flags);
		hl.inc();
		h.put(hl.loadHigh());
		l.put(hl.loadLow());
	}

	public boolean isOperationQueueEmpty()
	{
		return operationQueue.isEmpty();
	}

	public TickRequiertFunction pollFromOperationQueue()
	{
		return operationQueue.poll();
	}

	public Instruction get(String opCode)
	{
		return instructions.get(opCode);
	}
}
