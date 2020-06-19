import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.net.*;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;


public class client extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Boolean isOrderTable = false;
	private String[] goodsList;
	
	private void setStorageEditTable() {
		table.setModel(new DefaultTableModel(getStorageDB(),
				new String[] {
					"\u2116", "\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435", "\u041E\u0441\u0442\u0430\u043B\u043E\u0441\u044C \u043D\u0430 \u0441\u043A\u043B\u0430\u0434\u0435", "\u0426\u0435\u043D\u0430", "\u041F\u043E\u0441\u0442\u0430\u0432\u043A\u0430 \u0441\u043A\u043B\u0430\u0434" 
				}
			){
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(430);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
	}
	
	private void setOrdersTable() {
		table.setModel(new DefaultTableModel(getOrdersDB(),
				new String[] {
					"\u004e", "\u0424\u0418\u041e", "\u0422\u0435\u043b\u0435\u0444\u043e\u043d", "\u0410\u0434\u0440\u0435\u0441", "\u0421\u0443\u043c\u043c\u0430", "\u0414\u0430\u0442\u0430" 
				}
			){
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
	}
	
	private Object[][] getStorageDB() {
		
		try {
			URL url = new URL("http://www.minichan/storage.php?com=getTable");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.connect();
			Scanner input = new Scanner(connection.getInputStream(), "UTF-8");
			
			String inputLine = input.nextLine();
			
			
			String[] goodsDesc = inputLine.split("&");
			
			Object[][] rawtable = new Object[goodsDesc.length][];
			
			for (int i = 0; i < goodsDesc.length; ++i)
			{
				String[] goodDesc = goodsDesc[i].split(";");
				rawtable[i] = new Object[goodDesc.length];
				for (int j = 0; j < goodDesc.length; ++j)
					rawtable[i][j] = goodDesc[j];
			}
			
			input.close();
			connection.disconnect();
			connection = null;
			
			return rawtable;
		}
		catch (Exception exp) {}

		JOptionPane.showMessageDialog(null, "Не удалось подключиться к серверу", "Ошибка соединения", 0);
		return null;
	}
	
	private Object[][] getOrdersDB() {
		
		try {
			URL url = new URL("http://www.minichan/storage.php?com=getOrders");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.connect();
			Scanner input = new Scanner(connection.getInputStream(), "UTF-8");
			
			String inputLine = input.nextLine();
			
			String[] ordersDesc = inputLine.split("&");
			
			Object[][] rawtable = new Object[ordersDesc.length][];
			goodsList = new String[ordersDesc.length];
			
			for (int i = 0; i < ordersDesc.length; ++i)
			{
				String[] orderDesc = ordersDesc[i].split(";");
				rawtable[i] = new Object[orderDesc.length];
				for (int j = 0; j < orderDesc.length - 1; ++j)
					rawtable[i][j] = orderDesc[j];
				goodsList[i] = orderDesc[6].replace('!', '\n');
			}
			
			input.close();
			connection.disconnect();
			connection = null;
			
			return rawtable;
		}
		catch (Exception exp) {}

		JOptionPane.showMessageDialog(null, "Не удалось подключиться к серверу", "Ошибка соединения", 0);
		return null;
	}
	
	private String mageGetToUpdate() {
		String getRequest = "?com=update&";
		
		for (int i = 0; i < table.getRowCount(); ++i) {
			
			Integer amount = 0;
			Integer price = 0;
			
			try {
				String str = table.getValueAt(i, 3).toString().trim();
				amount = Integer.parseInt(str);
				str = table.getValueAt(i, 4).toString().trim();
				price = Integer.parseInt(str);
			}
			catch (Exception exp) {
				System.err.println("Found empty cell");
			}
			
			getRequest += "priceid" + (i + 1) + "=" + amount + "&" + "amountid" + (i + 1) + "=" + price + "&";
		}
		return getRequest;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client frame = new client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 780, 450);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		DefaultTableCellRenderer centerRend = new DefaultTableCellRenderer();
	    centerRend.setHorizontalAlignment(JLabel.CENTER);
	    table.setDefaultRenderer(String.class, centerRend);
		setStorageEditTable();
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && isOrderTable) {
					int row = table.rowAtPoint(e.getPoint());
					JFrame frameForOrdersList = new JFrame();
					frameForOrdersList.setBounds(100, 100, 300, 400);
					frameForOrdersList.setVisible(true);
					JTextPane paneForOrdersList = new JTextPane();
					paneForOrdersList.setBounds(5, 5, 90, 90);
					paneForOrdersList.setText(goodsList[row]);
					frameForOrdersList.getContentPane().add(paneForOrdersList);
				}
			}
		});
		JButton btnNewButton = new JButton("\u041F\u0440\u0438\u043C\u0435\u043D\u0438\u0442\u044C \u0438\u0437\u043C\u0435\u043D\u0435\u043D\u0438\u044F");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String str = "http://www.minichan/storage.php" + mageGetToUpdate();
				
				try {
					URL url = new URL(str);
					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
					
					connection.connect();
					Scanner input = new Scanner(connection.getInputStream(), "UTF-8");
					
					String inputLine = input.nextLine();
										
					String[] goodsDesc = inputLine.split("&");
					
					Object[][] rawtable = new Object[goodsDesc.length][];
					
					for (int i = 0; i < goodsDesc.length; ++i)
					{
						String[] goodDesc = goodsDesc[i].split("!");
						for (int j = 0; j < goodDesc.length; ++j)
							if (j == 1)
								table.setValueAt(goodDesc[j], i, j);
							else
								table.setValueAt(Integer.parseInt(goodDesc[j]), i, j);
					}
					
					input.close();
					connection.disconnect();
					connection = null;
					
					return;
				}
				catch (Exception exp) {
					
				}
				JOptionPane.showMessageDialog(null, "Не удалось подключиться к серверу", "Ошибка соединения", 0);
			}
		});
		btnNewButton.setBounds(100, 500, 240, 35);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u041F\u043E\u0441\u043C\u043E\u0442\u0440\u0435\u0442\u044C \u0442\u0430\u0431\u043B\u0438\u0446\u0443 \u0437\u0430\u043A\u0430\u0437\u043E\u0432");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isOrderTable) {
					btnNewButton_1.setBounds(300, 500, 240, 35);
					btnNewButton_1.setText("Просмотреть таблицу склада");
					btnNewButton.setVisible(false);
					setOrdersTable();
					isOrderTable = true;
				}
				else {
					btnNewButton_1.setBounds(450, 500, 240, 35);
					btnNewButton_1.setText("Просмотреть таблицу заказов");
					btnNewButton.setVisible(true);
					setStorageEditTable();
					isOrderTable = false;
				}
			}
		});
		btnNewButton_1.setBounds(450, 500, 240, 35);
		contentPane.add(btnNewButton_1);
	}
}
