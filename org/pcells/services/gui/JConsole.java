// $Id: JConsole.java,v 1.2 2004/06/21 22:30:27 cvs Exp $
//
package org.pcells.services.gui ;
//
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.* ;

public class JConsole extends JPanel 
        implements ActionListener, MouseListener {

   private JTextArea    _displayArea  = new JTextArea() ;
   private JScrollPane  _scrollPane   = null ;
   private JButton      _clearButton  = new JButton("Clear") ;

   public JConsole(){

      BorderLayout l = new BorderLayout() ;
      l.setVgap(10) ;
      l.setHgap(10);
      setLayout(l) ;
      _displayArea.setEditable(false);

      _scrollPane = new JScrollPane( _displayArea ) ;
      add( _scrollPane   , "Center" ) ;

      _displayArea.addMouseListener(this);
      _clearButton.addActionListener(this);

      JPanel south = new JPanel() ;
      l = new BorderLayout() ;
      l.setVgap(10) ;
      l.setHgap(10);

      south.setLayout(l) ;

      south.add( _clearButton  , "East" ) ;

//      add( south , "South" ) ;

      setBorder(

         BorderFactory.createCompoundBorder(
               BorderFactory.createTitledBorder("Console") ,
               BorderFactory.createEmptyBorder(8,8,8,8)
         )

      ) ;

    }
    public void actionPerformed( ActionEvent event ){
       Object source = event.getSource() ;
       if( source == _clearButton ){
          _displayArea.setText("");
       }
    }
    public void clear(){
       _displayArea.setText("");
    }
    public void setText( String text ){
       _displayArea.setText(text);
    }
    public void append( String text ){
       _displayArea.append(text);
       SwingUtilities.invokeLater(

          new Runnable(){
             public void run(){
                 Rectangle rect = _displayArea.getBounds() ;
                 rect.y = rect.height - 30 ;
                 _scrollPane.getViewport().scrollRectToVisible( rect ) ;
             }
          }
      ) ;
    }
    public void mouseClicked( MouseEvent event ){
      if( event.getClickCount() > 1 )_displayArea.setText("");
    }
    public void mouseEntered( MouseEvent event ){
    }
    public void mouseExited( MouseEvent event ){
    }
    public void mousePressed( MouseEvent event ){
    }
    public void mouseReleased( MouseEvent event ){
    }
}


