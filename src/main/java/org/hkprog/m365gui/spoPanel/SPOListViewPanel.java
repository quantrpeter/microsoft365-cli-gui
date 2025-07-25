package org.hkprog.m365gui.spoPanel;

import hk.quantr.javalib.CommonLib;
import javax.swing.SwingWorker;
import org.hkprog.m365gui.MainFrame;
import org.hkprog.m365gui.MyLib;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class SPOListViewPanel extends javax.swing.JPanel {

	String webUrl;
	String rootSiteUrl;
	String listTitle;
	String listId;

	public SPOListViewPanel(String rootSiteUrl, String webUrl, String listTitle, String listId) {
		this.rootSiteUrl = rootSiteUrl;
		this.webUrl = webUrl;
		this.listTitle = listTitle;
		this.listId = listId;
		initComponents();

		loadListItemInBackground();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
	 * Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();
        autoWidthButton = new javax.swing.JButton();
        filterTextField = new javax.swing.JTextField();
        exportExcelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        refreshButton.setText("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);

        autoWidthButton.setText("Auto Width");
        autoWidthButton.setFocusable(false);
        autoWidthButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        autoWidthButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        autoWidthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoWidthButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(autoWidthButton);

        filterTextField.setMaximumSize(new java.awt.Dimension(200, 23));
        jToolBar1.add(filterTextField);

        exportExcelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/famfamfam/page_white_excel.png"))); // NOI18N
        exportExcelButton.setText("Export Excel");
        exportExcelButton.setFocusable(false);
        exportExcelButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        exportExcelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exportExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportExcelButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(exportExcelButton);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        mainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        mainTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(mainTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
		loadListItemInBackground();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void autoWidthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoWidthButtonActionPerformed
		CommonLib.autoResizeColumnWithHeader(mainTable);
    }//GEN-LAST:event_autoWidthButtonActionPerformed

    private void exportExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportExcelButtonActionPerformed
		MyLib.exportTableToExcel(this, mainTable, listTitle.replaceAll("[^a-zA-Z0-9]", "_") + "_export.xlsx", listTitle);
    }//GEN-LAST:event_exportExcelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton autoWidthButton;
    private javax.swing.JButton exportExcelButton;
    private javax.swing.JTextField filterTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable mainTable;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

	private void loadListItemInBackground() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String command = MainFrame.setting.m365Path + " spo list view list --webUrl " + webUrl + " --listId " + listId + " --output json";
				System.out.println(command);
				String json = MyLib.run(command);

				JSONArray jsonArray = new JSONArray(json);
				setJsonToTable(jsonArray);
				return null;
			}

			@Override
			protected void done() {
				CommonLib.autoResizeColumnWithHeader(mainTable);
			}
		};
		worker.execute();
	}

	private void setJsonToTable(JSONArray jsonArray) {
		if (jsonArray.length() == 0) {
			return;
		}
		// Collect all unique keys as columns
		Set<String> columns = new LinkedHashSet<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			columns.addAll(obj.keySet());
		}
		String[] colArr = columns.toArray(new String[0]);
		Object[][] data = new Object[jsonArray.length()][colArr.length];
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			for (int j = 0; j < colArr.length; j++) {
				Object val = obj.has(colArr[j]) ? obj.get(colArr[j]) : null;
				if (val instanceof JSONObject || val instanceof JSONArray) {
					data[i][j] = val.toString();
				} else {
					data[i][j] = val;
				}
			}
		}
		DefaultTableModel model = new DefaultTableModel(data, colArr);
		mainTable.setModel(model);
	}
}
