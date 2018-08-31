package tools;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cpu.Accumulator;
import cpu.Flags;
import cpu.ProgrammCounter;
import cpu.Register16bit;
import cpu.Register8Bit;
import ram.Ram;

public class CPUDataScreen extends JFrame implements Runnable
{

	private Ram ram;
	private Accumulator accu;
	private Flags flags;
	private Register16bit sp;
	private ProgrammCounter pc;
	private Register8Bit b;
	private Register8Bit c;
	private Register8Bit d;
	private Register8Bit e;
	private Register8Bit h;
	private Register8Bit l;

	JLabel register = new JLabel("Register");
	JLabel biner = new JLabel("Binär");
	JLabel decimal = new JLabel("Dezimal");
	JLabel octal = new JLabel("Octal");
	JLabel hex = new JLabel("Hex");

	JLabel aLabel = new JLabel("A");
	JLabel aValueB = new JLabel();
	JLabel aValueO = new JLabel();
	JLabel aValueD = new JLabel();
	JLabel aValueH = new JLabel();

	JLabel flagsLabel = new JLabel("Flags Z N H C");
	JLabel flagsValueB = new JLabel();
	JLabel flagsValueO = new JLabel();
	JLabel flagsValueD = new JLabel();
	JLabel flagsValueH = new JLabel();

	JLabel bLabel = new JLabel("B");
	JLabel bValueB = new JLabel();
	JLabel bValueO = new JLabel();
	JLabel bValueD = new JLabel();
	JLabel bValueH = new JLabel();

	JLabel cLabel = new JLabel("C");
	JLabel cValueB = new JLabel();
	JLabel cValueO = new JLabel();
	JLabel cValueD = new JLabel();
	JLabel cValueH = new JLabel();

	JLabel dLabel = new JLabel("D");
	JLabel dValueB = new JLabel();
	JLabel dValueO = new JLabel();
	JLabel dValueD = new JLabel();
	JLabel dValueH = new JLabel();

	JLabel eLabel = new JLabel("E");
	JLabel eValueB = new JLabel();
	JLabel eValueO = new JLabel();
	JLabel eValueD = new JLabel();
	JLabel eValueH = new JLabel();

	JLabel hLabel = new JLabel("H");
	JLabel hValueB = new JLabel();
	JLabel hValueO = new JLabel();
	JLabel hValueD = new JLabel();
	JLabel hValueH = new JLabel();

	JLabel lLabel = new JLabel("L");
	JLabel lValueB = new JLabel();
	JLabel lValueO = new JLabel();
	JLabel lValueD = new JLabel();
	JLabel lValueH = new JLabel();

	JLabel spLabel = new JLabel("SP");
	JLabel spValueB = new JLabel();
	JLabel spValueO = new JLabel();
	JLabel spValueD = new JLabel();
	JLabel spValueH = new JLabel();

	JLabel pcLabel = new JLabel("PC");
	JLabel pcValueB = new JLabel();
	JLabel pcValueO = new JLabel();
	JLabel pcValueD = new JLabel();
	JLabel pcValueH = new JLabel();

	JLabel pcInstruction = new JLabel("Instruction");
	JLabel pcInstructionValueB = new JLabel();
	JLabel pcInstructionValueO = new JLabel();
	JLabel pcInstructionValueD = new JLabel();
	JLabel pcInstructionValueH = new JLabel();

	public CPUDataScreen(Ram ram, Accumulator accu, ProgrammCounter pc, Register16bit sp, Register8Bit b,
			Register8Bit c, Register8Bit d, Register8Bit e, Register8Bit h, Register8Bit l, Flags flags)
	{
		super("My Frame");

		this.ram = ram;
		this.flags = flags;
		this.accu = accu;
		this.pc = pc;
		this.sp = sp;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.h = h;
		this.l = l;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridLayout experimentLayout = new GridLayout(0, 5);

		this.getContentPane().setLayout(experimentLayout);

		this.getContentPane().add(register);
		this.getContentPane().add(biner);
		this.getContentPane().add(octal);
		this.getContentPane().add(decimal);
		this.getContentPane().add(hex);

		this.getContentPane().add(aLabel);
		this.getContentPane().add(aValueB);
		this.getContentPane().add(aValueO);
		this.getContentPane().add(aValueD);
		this.getContentPane().add(aValueH);

		this.getContentPane().add(flagsLabel);
		this.getContentPane().add(flagsValueB);
		this.getContentPane().add(flagsValueO);
		this.getContentPane().add(flagsValueD);
		this.getContentPane().add(flagsValueH);

		this.getContentPane().add(bLabel);
		this.getContentPane().add(bValueB);
		this.getContentPane().add(bValueO);
		this.getContentPane().add(bValueD);
		this.getContentPane().add(bValueH);

		this.getContentPane().add(cLabel);
		this.getContentPane().add(cValueB);
		this.getContentPane().add(cValueO);
		this.getContentPane().add(cValueD);
		this.getContentPane().add(cValueH);

		this.getContentPane().add(dLabel);
		this.getContentPane().add(dValueB);
		this.getContentPane().add(dValueO);
		this.getContentPane().add(dValueD);
		this.getContentPane().add(dValueH);

		this.getContentPane().add(eLabel);
		this.getContentPane().add(eValueB);
		this.getContentPane().add(eValueO);
		this.getContentPane().add(eValueD);
		this.getContentPane().add(eValueH);

		this.getContentPane().add(hLabel);
		this.getContentPane().add(hValueB);
		this.getContentPane().add(hValueO);
		this.getContentPane().add(hValueD);
		this.getContentPane().add(hValueH);

		this.getContentPane().add(lLabel);
		this.getContentPane().add(lValueB);
		this.getContentPane().add(lValueO);
		this.getContentPane().add(lValueD);
		this.getContentPane().add(lValueH);

		this.getContentPane().add(spLabel);
		this.getContentPane().add(spValueB);
		this.getContentPane().add(spValueO);
		this.getContentPane().add(spValueD);
		this.getContentPane().add(spValueH);

		this.getContentPane().add(pcLabel);
		this.getContentPane().add(pcValueB);
		this.getContentPane().add(pcValueO);
		this.getContentPane().add(pcValueD);
		this.getContentPane().add(pcValueH);

		this.getContentPane().add(pcInstruction);
		this.getContentPane().add(pcInstructionValueB);
		this.getContentPane().add(pcInstructionValueO);
		this.getContentPane().add(pcInstructionValueD);
		this.getContentPane().add(pcInstructionValueH);

		update();

		setSize(500, 500);
		setVisible(true);
	}

	private String ByteToString(byte value)
	{
		return Integer.toString(Byte.toUnsignedInt(value));
	}

	private String ByteToHexString(byte value)
	{
		return Integer.toHexString(Byte.toUnsignedInt(value));
	}

	private String ShortToString(short value)
	{
		return Integer.toString(Short.toUnsignedInt(value));
	}

	private String ShortToHexString(short value)
	{
		return Integer.toHexString(Short.toUnsignedInt(value));
	}

	private String ByteToOctalString(byte value)
	{
		StringBuilder octalNumber = new StringBuilder(Integer.toOctalString(Byte.toUnsignedInt(value)));
		octalNumber.reverse();
		while (octalNumber.length() < 3)
		{
			octalNumber.append("0");
		}
		return octalNumber.reverse().toString();
	}

	private String ByteToBinaryString(byte value)
	{
		StringBuilder octalNumber = new StringBuilder(Integer.toBinaryString(Byte.toUnsignedInt(value)));
		octalNumber.reverse();
		while (octalNumber.length() < 8)
		{
			octalNumber.append("0");
		}
		return octalNumber.reverse().toString();
	}

	private void update()
	{
		aValueB.setText(ByteToBinaryString(accu.load()));
		aValueO.setText(ByteToOctalString(accu.load()));
		aValueD.setText(ByteToString(accu.load()));
		aValueH.setText(ByteToHexString(accu.load()));

		flagsValueB.setText(ByteToBinaryString(flags.load()));
		flagsValueO.setText(ByteToOctalString(flags.load()));
		flagsValueD.setText(ByteToString(flags.load()));
		flagsValueH.setText(ByteToHexString(flags.load()));

		bValueB.setText(ByteToBinaryString(b.load()));
		bValueO.setText(ByteToOctalString(b.load()));
		bValueD.setText(ByteToString(b.load()));
		bValueH.setText(ByteToHexString(b.load()));

		cValueB.setText(ByteToBinaryString(c.load()));
		cValueO.setText(ByteToOctalString(c.load()));
		cValueD.setText(ByteToString(c.load()));
		cValueH.setText(ByteToHexString(c.load()));

		dValueB.setText(ByteToBinaryString(d.load()));
		dValueO.setText(ByteToOctalString(d.load()));
		dValueD.setText(ByteToString(d.load()));
		dValueH.setText(ByteToHexString(d.load()));

		eValueB.setText(ByteToBinaryString(e.load()));
		eValueO.setText(ByteToOctalString(e.load()));
		eValueD.setText(ByteToString(e.load()));
		eValueH.setText(ByteToHexString(e.load()));

		hValueB.setText(ByteToBinaryString(h.load()));
		hValueO.setText(ByteToOctalString(h.load()));
		hValueD.setText(ByteToString(h.load()));
		hValueH.setText(ByteToHexString(h.load()));

		lValueB.setText(ByteToBinaryString(l.load()));
		lValueO.setText(ByteToOctalString(l.load()));
		lValueD.setText(ByteToString(l.load()));
		lValueH.setText(ByteToHexString(l.load()));

		spValueD.setText(ShortToString(sp.load()));
		spValueH.setText(ShortToHexString(sp.load()));

		pcValueD.setText(ShortToString(pc.loadWithoutInc()));
		pcValueH.setText(ShortToHexString(pc.loadWithoutInc()));

		pcInstructionValueO.setText(ByteToOctalString(ram.load(pc.loadWithoutInc())));
		pcInstructionValueH.setText(ByteToHexString(ram.load(pc.loadWithoutInc())));
	}

	@Override
	public void run()
	{
		while (true)
		{
			update();
		}

	}
}
