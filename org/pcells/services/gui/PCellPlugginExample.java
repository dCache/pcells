// $Id: PCellPlugginExample.java,v 1.2 2005/07/04 06:10:14 cvs Exp $
//
package org.pcells.services.gui ;
//
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.* ;
import java.util.prefs.* ;
import org.pcells.services.connection.DomainConnection ;
import org.pcells.services.connection.DomainConnectionListener ;
import org.pcells.services.connection.DomainEventListener ;
import org.pcells.services.gui.* ;

//
//   Minimal example of a pCells pluggin.
//   Displays a big text area which is intended to display
//   the output from the server and two small text fields.
//   The left one should contain the name of the cell, this
//   application should talk to and the right one, the command
//   the application wants to send to the cell. On typing 
//   'return' in the command text field the command is send
//   to the destination cell and on return of the answer,
//   the answer is displayed in the center text area.
//
public class      PCellPlugginExample
       extends    CellGuiSkinHelper.CellPanel 
       implements DomainConnectionListener,
                  ActionListener{
                  
   private DomainConnection _connection  = null ;
   private Preferences      _preferences = null ;
   private JTextArea        _displayArea  = new JTextArea() ;
   private JScrollPane      _scrollPane   = null ;
   private Font             _displayFont = new Font("Monospaced",Font.PLAIN,12);
   private JTextField       _destination = new JTextField("System",20) ;
   private JTextField       _command     = new JTextField("") ;
   
   public PCellPlugginExample( DomainConnection connection , Preferences preferences ){
   
      _connection  = connection ;
      _preferences = preferences ;
      BorderLayout l = new BorderLayout(10,10) ;
      setLayout(l) ;
      
      setBorder( new CellGuiSkinHelper.CellBorder("PCell Example" ,25 ) ) ;   

     _displayArea.setEditable(false);
     _displayArea.setFont( _displayFont ) ;
     _scrollPane = new JScrollPane( _displayArea ) ;
     
      add( _scrollPane   , "Center" ) ;
      
      JPanel c = new JPanel( new BorderLayout(10,10) ) ;
      
      c.add( _destination , "West" ) ;
      c.add( _command     , "Center" ) ;
      
      _destination.setHorizontalAlignment(JTextField.LEFT);
      _destination.addActionListener(this);
      _command.setHorizontalAlignment(JTextField.LEFT);
      _command.addActionListener(this);
      
      add( c  , "South");
      
   }
   //
   //   Resolves swing interface :
   //       ActionListener 
   //
   //   is called if a 'enter' is typed in the command text field.
   //   Sends the command to the cell which it finds in the
   //   Destination text Field.
   //
   public void actionPerformed( ActionEvent event ){
      Object source = event.getSource() ;
      System.out.println("action : "+source.getClass().getName()+" "+event);
      if( source == _command ){
         if( _command.equals("") )return ;
         sendCommand( _command.getText() , _destination.getText() ) ;
         _command.setText("");      
      }
   }
   //
   // Resolves interface : 
   //
   //     org.pcells.services.connection.DomainConnectionListener
   //
   //  is called whenever the server sends an answer with its 
   //  timeout. Sends the content of the reply to the text area.
   //  If the reply an object array, it displays the 'toString'
   //  of all array elements.
   //
   public void domainAnswerArrived( Object obj , int subid ){
      System.out.println( "Answer ("+subid+") : "+obj.toString() ) ;
      if( obj instanceof Object [] ){
         Object [] array = (Object [])obj ;
         for( int i = 0 , n = array.length ; i < n ;i++ ){
            append(array[i].toString());
            append("\n");
         }
      }else{
         append(obj.toString());
         append("\n");
      }
   }
   //
   //  HELPER :
   //     send the command 'command' to the cell destination 'destination'
   //
   private void sendCommand( String command , String destination ){
     try{
        if( ( destination == null ) || ( destination.equals("") ) )
           _connection.sendObject(command,this,5);
        else
           _connection.sendObject(destination,command,this,5);
     }catch(Exception ee ){
        append(" EXCEPTION WHEN SENDING COMMAND : "+ee );
     }
   }
   //
   // HELPER :
   //    adds text to the display area scrolls to the end of the
   //    area.
   //
   private void append( String text ){
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
   
   
}
