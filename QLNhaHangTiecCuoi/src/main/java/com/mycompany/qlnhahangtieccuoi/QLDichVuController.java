/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.qlnhahangtieccuoi;

import com.mycompany.conf.Utils;
import com.mycompany.pojo.Services;
import com.mycompany.services.DichVuServices;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class QLDichVuController implements Initializable {
    @FXML TableView<Services> tbService;
    @FXML TextField txtKeyword;
    @FXML TextField txtServiceName;
    @FXML TextField txtServicePrice;
    @FXML Label lbMess;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
        public void initialize(URL url, ResourceBundle rb) {
        // TODO
        init();
        LoadTableView();
        try {
            LoadTableData(null);
        } catch (SQLException ex) {
            Logger.getLogger(QLDichVuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.txtKeyword.textProperty().addListener((evt)-> {
            try {
                this.LoadTableData(this.txtKeyword.getText());
            } catch (SQLException ex) {
                Logger.getLogger(QLDichVuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
    private void LoadTableView(){        
        TableColumn colName = new TableColumn("T??n d???ch v???");
        colName.setCellValueFactory(new PropertyValueFactory("serviceName"));
        colName.setPrefWidth(350);
        
        TableColumn colPrice = new TableColumn("Gi?? d???ch v???");
        colPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colPrice.setPrefWidth(150);
        
        this.tbService.getColumns().addAll(colName, colPrice);
    }
    
    private void LoadTableData(String kw) throws SQLException{
        DichVuServices s = new DichVuServices();
        this.tbService.setItems(FXCollections.observableArrayList(s.getServices(kw)));
    }
    
    @FXML
    private void BtrAddService(ActionEvent event) throws SQLException{
        try{
            String serviceName = txtServiceName.getText();
            double unitprice = Double.parseDouble(txtServicePrice.getText());
            Services s = new Services(serviceName, unitprice);
            DichVuServices dv = new DichVuServices();
            if (dv.kiemTraTonTai(serviceName)){
                dv.AddService(s);
                LoadTableData(null);
                Utils.getBox("Th??m th??nh c??ng", Alert.AlertType.INFORMATION).show();
                init();
            }
            else
                lbMess.setText("M??n ??n ???? t???n t???i");
        }catch (NumberFormatException ex2){
            lbMess.setText("?? ????n gi?? ph???i nh???p s???");
        }catch(SQLIntegrityConstraintViolationException ex3){
            lbMess.setText("B???n ph???i ??i???n ????? c??c ?? d??? li???u");
        }
    }
    @FXML
    private void BtrUpdateService(ActionEvent event) throws SQLException{
        Services service = tbService.getSelectionModel().getSelectedItem();
        if (service != null){
            try{
            int serID = service.getServiceID();
            String serviceName = txtServiceName.getText();
            double unitprice = Double.parseDouble(txtServicePrice.getText());
            DichVuServices dv = new DichVuServices();
            dv.UpdateService(serID, serviceName, unitprice);
            LoadTableData(null);      
            Utils.getBox("S???a th??nh c??ng", Alert.AlertType.INFORMATION).show();
            init();           
            }catch (NumberFormatException ex2){
                lbMess.setText("?? ????n gi?? ph???i nh???p s???");
            } 
        }
        else
            lbMess.setText("Ch??a ch???n ?????i t?????ng ????? s???a");
    }
    @FXML
    private void BtrDeleteService(ActionEvent event) throws SQLException{
        Services service = tbService.getSelectionModel().getSelectedItem();
        if (service != null){
            int serID = service.getServiceID();
            DichVuServices dv = new DichVuServices();
            dv.DeleteService(serID);
            LoadTableData(null);
            Utils.getBox("Xo?? th??nh c??ng", Alert.AlertType.INFORMATION).show();
            init();
        }
        else
            lbMess.setText("Ch??a ch???n ?????i t?????ng ????? xo??");
    }
    
    @FXML
    private void handleClickTableView(MouseEvent click){
        Services service = tbService.getSelectionModel().getSelectedItem();
        if (service != null){
            txtServiceName.setText(service.getServiceName());
            txtServicePrice.setText(String.valueOf(service.getUnitPrice()));
        }
    }
    private void init(){
        this.lbMess.setText(null);
        this.txtKeyword.setText(null);
        this.txtServiceName.setText(null);
        this.txtServicePrice.setText("0");
    }
    
    @FXML
    private void restrictNumbersOnly(KeyEvent keyEvent) {
        this.txtServicePrice.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*")) {
            txtServicePrice.setText(newValue.replaceAll("[^\\d]", ""));
        }
    });
    }
}
