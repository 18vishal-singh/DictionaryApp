
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.event.*;
import java .sql.*;

public class Mydictionary  implements KeyListener, ActionListener
{
	final String fileName = "D:\\JAVA\\database\\db1.accdb";
	JTable table;
	JFrame frame;
	JLabel jl;
	DefaultTableModel model = new DefaultTableModel();
	int sr_no=0;
	String word="";
	String meaning="";
	PreparedStatement st;
	Connection c;
	ResultSet rs;
	
	static JTextField jtf,jtf1,jtf2,jtf3,jtf4;
	JButton b1,b2,b3,b4,b5;
	
	String[] columnNames = {"sr_no","Word","Meaning"};
	
	Mydictionary() throws Exception
	{
	//	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	//	c=DriverManager.getConnection("jdbc:odbc:mydsn","system","vis");
		
	//	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	//	String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb,*.accdb)};DBQ="+fileName;
	//	c = DriverManager.getConnection(url,"system","vis");


		frame = new JFrame("WORD-MEANINGS");
      		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      	
      		jtf=new JTextField("Word");
		jtf.setBounds(20,340,250,25);
		jtf1=new JTextField("Meaning");
		jtf1.setBounds(280,340,440,25);
		jtf2=new JTextField("Edit A Word");
		jtf2.setBounds(20,380,250,25);
		jtf3=new JTextField("Delete a word");
		jtf3.setBounds(20,420,250,25);
		jtf4=new JTextField("Find A Word");
		jtf4.setBounds(470, 380,250,25);
		
		jtf.addKeyListener(this);
		jtf4.addKeyListener(this);
		
		jl=new JLabel("Search a Word : ");
		jl.setBounds(380, 380, 100, 25);
		
		b1=new JButton("ADD");
		b1.setBounds(730, 340, 80, 25);
		b1.addActionListener(this);
		b2=new JButton("EDIT");
		b2.addActionListener(this);
		b2.setBounds(280, 380, 80, 25);
		b3=new JButton("DELETE");
		b3.setBounds(280, 420, 80, 25);
		b3.addActionListener(this);
		b4=new JButton("SHOW ALL");
		b4.setBounds(500, 420, 100, 25);
		b4.addActionListener(this);
		b5=new JButton("EXIT");
		b5.setBounds(640, 420, 80, 25);
		b5.addActionListener(this);
		
		
		
		jtf4.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		        jtf4.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	jtf4.setText("Find A Word");
		    }
		});
		
		frame.add(jtf);
		frame.add(jtf1);
		frame.add(jtf2);
		frame.add(jtf3);
		frame.add(jtf4);
		frame.add(b1);
		frame.add(b2);
		frame.add(b3);
		frame.add(b4);
		frame.add(b5);
		frame.add(jl);
      	
      	model.setColumnIdentifiers(columnNames);
      	table = new JTable(model);
		table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(250);
        table.getColumnModel().getColumn(1).setMinWidth(250);
        

     	table.setPreferredScrollableViewportSize(new Dimension(800, 300));
		table.setFillsViewportHeight(true);						//Sets whether or not this table is always made large enough to fill the height of an enclosing viewport.
		frame.add(new JScrollPane(table));
      	
      	frame.pack();
    	frame.setVisible(true);
    	frame.setLayout(null);
    	frame.setSize(850,500);

	}
	public static void main(String arg[]) throws Exception
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {}
		new Mydictionary();
	
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("EXIT"))
		{
			System.exit(0);
		}
		if(e.getActionCommand().equals("ADD"))
		{
			String str1="";
			String str2="";
			str1=jtf.getText();
			str2=jtf1.getText();
			try
			{	
				Statement s=c.createStatement();
				int x =s.executeUpdate("insert into Meaning values('"+str1+"','"+str2+"')");
				if(x==1)
				{
					jtf.setText("Word");
					jtf1.setText("Sucessfully added");
					jtf2.setText("Edit A Word");
				}
				if(x==0)
				{
					jtf1.setText("word can't be added");
				}
			}
			catch(Exception ex){jtf1.setText("Word already exist");}
			
		}
		if(e.getActionCommand().equals("DELETE"))
		{
			String str1="";
			str1=jtf3.getText();
			try
			{
				Statement s=c.createStatement();
				int x =s.executeUpdate("delete from Meaning where Word='"+str1+"'");
				if(x==0)
				{
					jtf3.setText("Word not found");
				}
				if(x==1)
				{
					jtf3.setText("Sucessfully deleted");
				}
			}
			catch(Exception ex){}
			
		}
		if(e.getActionCommand().equals("EDIT"))
		{
			String str="";
			str=jtf2.getText();
			System.out.println("testing");
			try
			{
				st=c.prepareStatement("select * from Meaning where Word like ?");
				st.setString(1,str);
				rs = st.executeQuery();
				rs.next();
				Statement s=c.createStatement();
				jtf.setText(rs.getString("Word"));
				jtf1.setText(rs.getString("Meaning"));
				int x =s.executeUpdate("delete from Meaning where Word='"+str+"'");
			}
			catch(Exception ex){}
			
		}
		if(e.getActionCommand().equals("SHOW ALL"))
		{
			model.setRowCount(0);
			try
			{
				st=c.prepareStatement("select * from Meaning");
				rs = st.executeQuery();
				Integer i=new Integer(1);
				while (rs.next()) 
				{
					word=rs.getString("Word");
					meaning=rs.getString("Meaning");
					model.addRow(new Object[]{i,word,meaning});
					i = Integer.valueOf(i.intValue() + 1);
	        	
				}
			}
			catch(Exception ex){}
		}
	}
	public void keyPressed(KeyEvent e) 
	{
    }
	public void keyTyped(KeyEvent e) 
	{
    
	}
	public void keyReleased(KeyEvent e) 
	{
		String str="";
		model.setRowCount(0);
		if(!jtf.getText().equals("Word"))
		{
			str=jtf.getText();
			str=str+"%";
		}
		else
		{
			str=jtf4.getText();
			str=str+"%";
		}
		try
		{
			st=c.prepareStatement("select * from Meaning where Word like ?");
			st.setString(1,str);
			rs = st.executeQuery();
			Integer i=new Integer(1);
			while (rs.next()) 
			{
				word=rs.getString("Word");
				meaning=rs.getString("Meaning");
				model.addRow(new Object[]{i,word,meaning});
				i = Integer.valueOf(i.intValue() + 1);
        	
			}
		}
		catch(Exception ex){}
	}

}
