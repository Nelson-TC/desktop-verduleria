package org.example.themes.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    public CustomButton(String text, Color normalBackgroundColor, Color focusBackgroundColor) {
        super(text);

        // Establece el color de fondo normal
        setBackground(normalBackgroundColor);

        // Agrega un MouseListener para gestionar los eventos de hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambia el color de fondo cuando el mouse entra en el botón (hover)
                setBackground(focusBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaura el color de fondo normal cuando el mouse sale del botón
                setBackground(normalBackgroundColor);
            }
        });

        // Agrega un FocusListener para gestionar los eventos de enfoque
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                // Cambia el color de fondo cuando el botón obtiene el foco
                setBackground(focusBackgroundColor);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                // Restaura el color de fondo normal cuando el botón pierde el foco
                setBackground(normalBackgroundColor);
            }
        });
    }
}
