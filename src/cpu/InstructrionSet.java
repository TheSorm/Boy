package cpu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import cpu.CPU.State;
import ram.Ram;

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

	private interface ByteInputOperation
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

		HashMap<Character, ByteInputOperation> opCodeToOperation = new HashMap<>();
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
					() -> loadDirect16ToRegister(pc, ram, from.getValue().first, from.getValue().second));
			instructions.put("0" + from.getKey() + "2",
					() -> putRegisterInRamAdressedByRegister(ram, from.getValue().first, from.getValue().second, accu));
			instructions.put("0" + from.getKey() + "3",
					() -> inc16BitRegister(flags, from.getValue().first, from.getValue().second));

			instructions.put("3" + from.getKey() + "1",
					() -> popToRegister(sp, ram, from.getValue().first, from.getValue().second));
			instructions.put("3" + from.getKey() + "5",
					() -> pushFromRegister(sp, ram, from.getValue().first, from.getValue().second));
		}

		for (Entry<Character, RegisterPair> from : opCodeToRegisterPairHigh.entrySet())
		{
			instructions.put("0" + from.getKey() + "1",
					() -> addRegisterToHL(flags, h, l, from.getValue().first, from.getValue().second));
			instructions.put("0" + from.getKey() + "2", () -> loadRamAdressedByRegisterInRegister(ram,
					from.getValue().first, from.getValue().second, accu));
			instructions.put("0" + from.getKey() + "3",
					() -> dcr16BitRegister(flags, from.getValue().first, from.getValue().second));
		}

		instructions.put("000", () -> {
		});
		instructions.put("010", () -> loadStackPointerToDirect16(pc, sp, flags, ram));
		instructions.put("020", () -> {
			cpu.setState(State.STOP);
		});
		instructions.put("030", () -> conditionalJumpAddSignedImeadiate8(pc, ram, true));
		instructions.put("040", () -> conditionalJumpAddSignedImeadiate8(pc, ram, !flags.isZeroFlag()));
		instructions.put("050", () -> conditionalJumpAddSignedImeadiate8(pc, ram, flags.isZeroFlag()));
		instructions.put("060", () -> conditionalJumpAddSignedImeadiate8(pc, ram, !flags.isCarryFlag()));
		instructions.put("070", () -> conditionalJumpAddSignedImeadiate8(pc, ram, flags.isCarryFlag()));

		instructions.put("007", () -> accu.rlca());
		instructions.put("017", () -> accu.rrca());
		instructions.put("027", () -> accu.rla());
		instructions.put("037", () -> accu.rra());
		instructions.put("047", () -> accu.da());
		instructions.put("057", () -> accu.cpl());
		instructions.put("067", () -> flags.scf());
		instructions.put("077", () -> flags.ccf());

		instructions.put("042", () -> {
			operationQueue.add(() -> {
				ram.put(h.load(), l.load(), accu.load());
				incHL(h, l, flags);
			});
		});
		instructions.put("052", () -> {
			operationQueue.add(() -> {
				accu.put(ram.load(h.load(), l.load()));
				incHL(h, l, flags);
			});
		});
		instructions.put("062", () -> {
			operationQueue.add(() -> {
				ram.put(h.load(), l.load(), accu.load());
				dcrHL(h, l, flags);
			});
		});
		instructions.put("072", () -> {
			operationQueue.add(() -> {
				accu.put(ram.load(h.load(), l.load()));
				dcrHL(h, l, flags);
			});
		});

		instructions.put("061", () -> loadDirect16ToRegister(pc, ram, sp));
		instructions.put("071", () -> addRegisterToHL(flags, ram, h, l, sp));

		instructions.put("063", () -> inc16BitRegister(sp));
		instructions.put("073", () -> dcr16BitRegister(sp));

		instructions.put("064", () -> incWhereHLPoints(flags, ram, h, l));
		instructions.put("065", () -> dcrWhereHLPoints(flags, ram, h, l));
		instructions.put("066", () -> loadDirect8ToWhereHLPoints(pc, ram, h, l));

		for (Map.Entry<Character, Register8Bit> from : opCodeToRegister.entrySet())
		{
			instructions.put("0" + from.getKey() + "4", () -> from.getValue().inc());
			instructions.put("0" + from.getKey() + "5", () -> from.getValue().dcr());
			instructions.put("0" + from.getKey() + "6", () -> putImmediate8InRegister(pc, ram, from.getValue()));

			for (Map.Entry<Character, Register8Bit> into : opCodeToRegister.entrySet())
			{
				instructions.put("1" + into.getKey() + from.getKey(),
						() -> into.getValue().put(from.getValue().load()));
			}

			instructions.put("1" + from.getKey() + "6",
					() -> this.loadRamAdressedByRegisterInRegister(ram, h, l, from.getValue()));

			instructions.put("1" + "6" + from.getKey(),
					() -> this.putRegisterInRamAdressedByRegister(ram, h, l, from.getValue()));
		}

		instructions.put("166", () -> {
			cpu.setState(State.HALT);
		});

		for (Map.Entry<Character, ByteInputOperation> operation : opCodeToOperation.entrySet())
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
			instructions.put("3" + opKey + "7", () -> restartAt(pc, sp, ram, adress));
		}

		for (Entry<Character, Condition> condition : opCodeToCondition.entrySet())
		{
			instructions.put("3" + condition.getKey() + "0",
					() -> conditionalReturn(pc, sp, ram, condition.getValue().evaluate()));
			instructions.put("3" + condition.getKey() + "2",
					() -> conditionalJumpToDirect16(pc, ram, condition.getValue().evaluate()));
			instructions.put("3" + condition.getKey() + "4",
					() -> conditionalCall(pc, sp, ram, condition.getValue().evaluate()));

		}

		instructions.put("303", () -> conditionalJumpToDirect16(pc, ram, true));
		instructions.put("311", () -> normalReturn(pc, sp, ram));
		instructions.put("313", () -> this.invokeCBInstruction(pc, ram, cpu));
		instructions.put("315", () -> conditionalCall(pc, sp, ram, true));
		instructions.put("331", () -> {
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
		});

		instructions.put("340", () -> loadRegisterToHighAndDirect8(pc, ram, accu));
		instructions.put("342",
				() -> putRegisterInRamAdressedByRegister(ram, new Register8Bit((byte) 255, flags), c, accu));

		instructions.put("350", () -> addSignedDirect8ToRegister(pc, ram, sp));
		instructions.put("351", () -> {
			pc.put(h.load(), l.load());
		});
		instructions.put("352", () -> putRegisterInRamAdressedByImmediate16(pc, ram, accu));

		instructions.put("360", () -> loadHighAndDirect8ToRegister(pc, ram, accu));
		instructions.put("361", () -> popToRegister(sp, ram, accu, flags));
		instructions.put("362",
				() -> loadRamAdressedByRegisterInRegister(ram, new Register8Bit((byte) 255, flags), c, accu));
		instructions.put("363", () -> {
			cpu.setInterruptMasterEnableImediate(false);
		});
		instructions.put("365", () -> pushFromRegister(sp, ram, accu, flags));

		instructions.put("370", () -> putSignedImmediate8PlusSPToHL(pc, flags, ram, h, l, sp));
		instructions.put("371", () -> loadRegisterToRegister(h, l, sp));
		instructions.put("372", () -> loadRamAdressedByImmediate16InRegister(pc, ram, accu));
		instructions.put("373", () -> {
			cpu.setInterruptMasterEnableDelayed(true);
		});

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

	private void invokeCBInstruction(ProgrammCounter pc, Ram ram, CPU cpu)
	{
		operationQueue.add(() -> {
			instructionsCB.get(cpu.loadOctalOpCode(pc, ram)).invoke();
		});
	}

	public void restartAt(ProgrammCounter pc, Register16bit sp, Ram ram, byte adress)
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

	public void restartAt(ProgrammCounter pc, Register16bit sp, Ram ram, InterruptFlagRegister adress,
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

	private void conditionalReturn(ProgrammCounter pc, Register16bit sp, Ram ram, boolean condition)
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

	private void normalReturn(ProgrammCounter pc, Register16bit sp, Ram ram)
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

	private void popToRegister(Register16bit sp, Ram ram, Register8Bit high, Register8Bit low)
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

	private void pushFromRegister(Register16bit sp, Ram ram, Register8Bit high, Register8Bit low)
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

	private void conditionalCall(ProgrammCounter pc, Register16bit sp, Ram ram, boolean condition)
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

	private void conditionalJumpAddSignedImeadiate8(ProgrammCounter pc, Ram ram, boolean condition)
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

	private void conditionalJumpToDirect16(ProgrammCounter pc, Ram ram, boolean condition)
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

	private void loadStackPointerToDirect16(ProgrammCounter pc, Register16bit sp, Flags flags, Ram ram)
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

	private void loadRegisterToRegister(Register8Bit from, Register8Bit and, Register16bit to)
	{
		operationQueue.add(() -> {
			to.put(from.load(), and.load());
		});
	}

	private void loadRamAdressedByRegisterInRegister(Ram ram, Register8Bit high, Register8Bit low, Register8Bit to)
	{
		operationQueue.add(() -> {
			to.put(ram.load(high.load(), low.load()));
		});
	}

	private void putRegisterInRamAdressedByRegister(Ram ram, Register8Bit high, Register8Bit low, Register8Bit from)
	{
		operationQueue.add(() -> {
			ram.put(high.load(), low.load(), from.load());
		});
	}

	private void loadRamAdressedByImmediate16InRegister(ProgrammCounter pc, Ram ram, Register8Bit register)
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

	private void putRegisterInRamAdressedByImmediate16(ProgrammCounter pc, Ram ram, Register8Bit register)
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

	private void loadHighAndDirect8ToRegister(ProgrammCounter pc, Ram ram, Register8Bit to)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			to.put(ram.load((byte) 255, lowShadow));
		});
	}

	private void loadRegisterToHighAndDirect8(ProgrammCounter pc, Ram ram, Register8Bit from)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put((byte) 255, lowShadow, from.load());
		});
	}

	private void putImmediate8InRegister(ProgrammCounter pc, Ram ram, Register8Bit register)
	{
		operationQueue.add(() -> {
			register.put(ram.load(pc.load()));
		});
	}

	private void loadDirect8ToWhereHLPoints(ProgrammCounter pc, Ram ram, Register8Bit h, Register8Bit l)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			ram.put(h.load(), l.load(), lowShadow);
		});
	}

	private void inc16BitRegister(Flags flags, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit toIncrement = new Register16bit(high.load(), low.load(), flags);

			toIncrement.inc();

			high.put(toIncrement.loadHigh());
			low.put(toIncrement.loadLow());
		});
	}

	private void inc16BitRegister(Register16bit register)
	{
		operationQueue.add(() -> {
			register.inc();
		});
	}

	private void dcr16BitRegister(Flags flags, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit toIncrement = new Register16bit(high.load(), low.load(), flags);

			toIncrement.dcr();

			high.put(toIncrement.loadHigh());
			low.put(toIncrement.loadLow());
		});
	}

	private void dcr16BitRegister(Register16bit register)
	{
		operationQueue.add(() -> {
			register.dcr();
		});
	}

	private void incWhereHLPoints(Flags flags, Ram ram, Register8Bit h, Register8Bit l)
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

	private void dcrWhereHLPoints(Flags flags, Ram ram, Register8Bit h, Register8Bit l)
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

	private void addRegisterToHL(Flags flags, Register8Bit h, Register8Bit l, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			Register16bit hl = new Register16bit(h.load(), l.load(), flags);
			hl.add(high.load(), low.load());

			h.put(hl.loadHigh());
			l.put(hl.loadLow());
		});
	}

	private void addRegisterToHL(Flags flags, Ram ram, Register8Bit h, Register8Bit l, Register16bit register)
	{
		operationQueue.add(() -> {
			Register16bit hl = new Register16bit(h.load(), l.load(), flags);
			hl.add(register.load());

			h.put(hl.loadHigh());
			l.put(hl.loadLow());
		});
	}

	private void putSignedImmediate8PlusSPToHL(ProgrammCounter pc, Flags flags, Ram ram, Register8Bit h, Register8Bit l,
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

	private void addSignedDirect8ToRegister(ProgrammCounter pc, Ram ram, Register16bit register)
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

	private void loadDirect16ToRegister(ProgrammCounter pc, Ram ram, Register8Bit high, Register8Bit low)
	{
		operationQueue.add(() -> {
			low.put(ram.load(pc.load()));
		});

		operationQueue.add(() -> {
			high.put(ram.load(pc.load()));
		});
	}

	private void loadDirect16ToRegister(ProgrammCounter pc, Ram ram, Register16bit register)
	{
		operationQueue.add(() -> {
			lowShadow = ram.load(pc.load());
		});

		operationQueue.add(() -> {
			register.put(ram.load(pc.load()), lowShadow);
		});

	}

	private void dcrHL(Register8Bit h, Register8Bit l, Flags flags)
	{
		Register16bit hl = new Register16bit(h.load(), l.load(), flags);
		hl.dcr();
		h.put(hl.loadHigh());
		l.put(hl.loadLow());
	}

	private void incHL(Register8Bit h, Register8Bit l, Flags flags)
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
