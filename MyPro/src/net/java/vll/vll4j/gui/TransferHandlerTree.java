package net.java.vll.vll4j.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.JTree.DropLocation;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.java.vll.vll4j.api.NodeBase;

class TransferHandlerTree
  extends TransferHandler
{
  public TransferHandlerTree()
  {
    try
    {
      this.dataFlavors = new DataFlavor[] { new DataFlavor(mimeType(NodeBase.class)) };
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
  
  public boolean canImport(TransferHandler.TransferSupport support)
  {
    if (!support.isDrop())
      return false;
    support.setShowDropLocation(true);
    if (!support.isDataFlavorSupported(this.dataFlavors[0])) 
    {
      return false;
    }
    JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
    Object par1 = dl.getPath().getLastPathComponent();
    
    if ((this.nodeToRemove == null) || (this.nodeToRemove.getParent() == null) || (par1 != this.nodeToRemove.getParent())) 
    {
      return false;
    }
    return true;
  }
  
  public Transferable createTransferable(JComponent c)
  {
    TreePath path = ((JTree)c).getSelectionPath();
    if (path != null) 
    {
      final NodeBase node = (NodeBase)path.getLastPathComponent();
      this.nodeToRemove = node;
      new Transferable()
      {
        public DataFlavor[] getTransferDataFlavors() 
        {
          return TransferHandlerTree.this.dataFlavors;
        }
        
        public boolean isDataFlavorSupported(DataFlavor flavor) 
        {
          return TransferHandlerTree.this.dataFlavors[0].equals(flavor);
        }
        
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
        {
          if (!isDataFlavorSupported(flavor)) 
          {
            throw new UnsupportedFlavorException(flavor);
          }
          return node.clone();
        }
      };
    }
    return null;
  }
  

  public void exportDone(JComponent source, Transferable data, int action)
  {
    if ((action & 0x2) == 2) 
    {
      DefaultTreeModel model = (DefaultTreeModel)((JTree)source).getModel();
      model.removeNodeFromParent(this.nodeToRemove);
    }
  }
  
  public int getSourceActions(JComponent c)
  {
    return 2;
  }
  
  public boolean importData(TransferHandler.TransferSupport support)
  {
    if (!canImport(support)) 
    {
      return false;
    }
    NodeBase node = null;
    try 
    {
      Transferable t = support.getTransferable();
      node = (NodeBase)t.getTransferData(this.dataFlavors[0]);
    } 
    catch (Exception ex) 
    {
      ex.printStackTrace();
    }
    
    JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
    int childIndex = dl.getChildIndex();
    TreePath dest = dl.getPath();
    NodeBase parent = (NodeBase)dest.getLastPathComponent();
    JTree tree = (JTree)support.getComponent();
    DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
    
    int index = childIndex;
    if (childIndex == -1) 
    {
      index = parent.getChildCount();
    }
    
    model.insertNodeInto(node, parent, index);
    return true;
  }
  
  private String mimeType(Class c) 
  {
    String mt = String.format("%s;class=\"%s\"", new Object[] { "application/x-java-jvm-local-objectref", c.getName() });
    return mt;
  }
  
  NodeBase nodeToRemove = null;
  DataFlavor[] dataFlavors = null;
}
