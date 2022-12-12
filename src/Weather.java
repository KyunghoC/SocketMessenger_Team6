import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Weather {
	
	private int locationCode;
    //private String date;
    //private String time;
    private double PTY; //�������� �ڵ尪
    private double REH; //���� %
    private double RN1; //1�ð� ������ ����(1mm)
    private double T1H; //��� ��
    private double UUU; //ǳ��(��������) m/s
    private double VVV; //ǳ��(���ϼ���) m/s
    private double VEC; //ǳ�� deg
    private double WSD; //ǳ�� m/s
    
    private String PTYForm; //(�ʴܱ�) ����(0), ��(1), ��/��(2), ��(3), �����(5), ����ﴫ����(6), ������(7) 
							//(�ܱ�) ����(0), ��(1), ��/��(2), ��(3), �ҳ���(4)
    public String setPTYForm(double PTY) {
    if(PTY==0) {
    	PTYForm="����";
    }
    else if(PTY==1) {
    	PTYForm="��";
    }
    else if(PTY==2) {
    	PTYForm="��/��";
    }
    else if(PTY==3) {
    	PTYForm="��";
    }
    else if(PTY==4) {
    	PTYForm="�ҳ���";
    }
    else if(PTY==5) {
    	PTYForm="�����";
    }
    else if(PTY==6) {
    	PTYForm="����ﴫ����";
    }
    else if(PTY==7) {
    	PTYForm="������";
    }
    else
    	PTYForm="error";
	return PTYForm;
    }
    
    LocalDate date = LocalDate.now();
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy�� MM�� dd��");
	
	LocalTime time = LocalTime.now();
	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH�� mm��");
	

    @Override
    public String toString() {
        return "locationCode = " + locationCode +
                "\n��¥ = " + date.format(formatter1) +
                "\n�ð� = " + time.format(formatter2) +
                "\n�������� = " +setPTYForm(PTY)+
                "\n���� = " + REH +"(%)"+
                "\n������ = " + RN1 +"(mm)"+
                "\n��� = " + T1H +"(��)"+
                "\nǳ��(����) = " + UUU +"(m/s)"+
                "\nǳ��(����) = " + VEC +"(m/s)"+
                "\nǳ�� = " + VVV +"(deg)"+
                "\nǳ�� = " + WSD +"(m/s)";
    }

	public void setPTY(double PTY) {
		this.PTY = PTY;
	}

	public void setREH(double REH) {
		this.REH = REH;
	}

	public void setRN1(double RN1) {
		this.RN1 = RN1;
	}

	public void setT1H(double T1H) {
		this.T1H = T1H;
	}

	public void setUUU(double UUU) {
		this.UUU = UUU;
	}

	public void setVEC(double VEC) {
		this.VEC = VEC;
	}

	public void setVVV(double VVV) {
		this.VVV = VVV;
	}

	public void setWSD(double WSD) {
		this.WSD = WSD;
	}
}
