/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

package test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

public class UndoRedoTextArea extends JFrame {
  protected JTextArea textArea = new JTextArea();

  protected UndoManager undoManager = new UndoManager();

  protected JButton undoButton = new JButton("Undo");

  protected JButton redoButton = new JButton("Redo");

  public UndoRedoTextArea() {
    super("Undo/Redo Demo");

    undoButton.setEnabled(false);
    redoButton.setEnabled(false);

    JPanel buttonPanel = new JPanel(new GridLayout());
    buttonPanel.add(undoButton);
    buttonPanel.add(redoButton);

    JScrollPane scroller = new JScrollPane(textArea);

    getContentPane().add(buttonPanel, BorderLayout.NORTH);
    getContentPane().add(scroller, BorderLayout.CENTER);

    textArea.getDocument().addUndoableEditListener(
        new UndoableEditListener() {
          public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
            updateButtons();
          }
        });

    undoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          undoManager.undo();
        } catch (CannotRedoException cre) {
          cre.printStackTrace();
        }
        updateButtons();
      }
    });

    redoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          undoManager.redo();
        } catch (CannotRedoException cre) {
          cre.printStackTrace();
        }
        updateButtons();
      }
    });

    setSize(400, 300);
    setVisible(true);
  }

  public void updateButtons() {
    undoButton.setText(undoManager.getUndoPresentationName());
    redoButton.setText(undoManager.getRedoPresentationName());
    undoButton.setEnabled(undoManager.canUndo());
    redoButton.setEnabled(undoManager.canRedo());
  }

  public static void main(String argv[]) {
    new UndoRedoTextArea();
  }
}