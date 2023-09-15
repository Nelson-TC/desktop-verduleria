package org.example.controller;

import org.example.service.ApiService;
import org.example.service.InventoryService;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(){
        this.inventoryService = new InventoryService(new ApiService());
    }

    public boolean increaseInventory(List<InventoryService.InventoryItem> items){
        try{
            inventoryService.increaseInventory(items);
            JOptionPane.showMessageDialog(null, "Se han aumentado los inventarios con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }catch(IOException ex){
            int statusCode = Integer.parseInt(ex.getMessage().substring(41, 44));
            if (statusCode == 400) {
                JOptionPane.showMessageDialog(null, "Verifica que todos los productos aun existan.");
            } else if (statusCode == 500) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error interno en el servidor.");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado, intentalo mas tarde.");
                ex.printStackTrace();
            }
            return false;
        }

    }
    public boolean decreaseInventory(List<InventoryService.InventoryItem> items){
        try{
            inventoryService.decreaseInventory(items);
            JOptionPane.showMessageDialog(null, "Se han disminuido los inventarios con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }catch(IOException ex){
            int statusCode = Integer.parseInt(ex.getMessage().substring(41, 44));
            if (statusCode == 400) {
                JOptionPane.showMessageDialog(null, "Verifica que los inventarios sean mayores a las cantidades a disminuir.");
            } else if (statusCode == 500) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error interno en el servidor.");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado, intentalo mas tarde.");
                ex.printStackTrace();
            }
            return false;
        }
    }
}
