/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.bvr.tool.ui.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import no.sintef.bvr.tool.common.Constants;
import no.sintef.bvr.tool.controller.command.UpdateVClassOccurence;
import no.sintef.bvr.tool.exception.RethrownException;
import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.controller.command.Command;
import no.sintef.bvr.tool.ui.editor.BVRUIKernel;
import no.sintef.bvr.ui.framework.elements.CustomTextField;
import bvr.VNode;
import bvr.PrimitiveTypeEnum;
import bvr.PrimitveType;
import bvr.VClassOccurrence;
import bvr.VSpec;
import bvr.Variable;

public class VClassOccurencePropertyEditor extends ElementPropertyEditor {
	
	private static final long serialVersionUID = 32837078208251642L;


	public VClassOccurencePropertyEditor(BVRUIKernel _kernel, Command _okcommand, VSpec elem, JComponent _node,
			BVRNotifiableController _controller) {
		super(_kernel, _okcommand, elem, _node, _controller);
		
        // Type
        JPanel pt = new JPanel(new SpringLayout());
        pt.setBorder(null);
        pt.setOpaque(false);
        
        JLabel lt = new JLabel("Type", JLabel.TRAILING);

        pt.add(lt);
        CustomTextField type = new CustomTextField(15);

        lt.setLabelFor(type);
        pt.add(type);
        
        String setType = (((VClassOccurrence) elem).getVType() != null) ? ((VClassOccurrence) elem).getVType().getName() : Constants.DEFAULT_TYPE_TITLE;
        type.setText(setType);
        ((UpdateVClassOccurence) command).setType(setType);

        top.add(pt);
        SpringUtilities.makeCompactGrid(pt,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        type.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));

        type.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                try {
                    ((UpdateVClassOccurence) command).setType(e.getDocument().getText(0, e.getDocument().getLength()));
                } catch (BadLocationException ex) {
                	throw new RethrownException("input error in Type", ex);
                }
            }

            public void removeUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }

            public void changedUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
        });
		
        //Lower bound
        JPanel p2 = new JPanel(new SpringLayout());
        p2.setBorder(null);
        p2.setOpaque(false);
		
        JLabel l2 = new JLabel("Lower", JLabel.TRAILING);
        p2.add(l2);
        
        final CustomTextField textField2 = new CustomTextField(15);

        l2.setLabelFor(textField2);
        p2.add(textField2);
        textField2.setText(String.valueOf(((VClassOccurrence) obj).getInstanceMultiplicity().getLower()));
        
        top.add(p2);
        SpringUtilities.makeCompactGrid(p2,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        
        //Upper bound
        JPanel p3 = new JPanel(new SpringLayout());
        p3.setBorder(null);
        p3.setOpaque(false);
        
        JLabel l3 = new JLabel("Upper", JLabel.TRAILING);
        //l3.setUI(new HudLabelUI());

        p3.add(l3);
        final CustomTextField textField3 = new CustomTextField(15);
        //textField3.setUI(new HudTextFieldUI());

        l3.setLabelFor(textField3);
        p3.add(textField3);
        Integer up = ((VClassOccurrence) obj).getInstanceMultiplicity().getUpper();
        if(up == -1)
        	textField3.setText("*");
        else
        	textField3.setText(String.valueOf(up));
        
        top.add(p3);
        SpringUtilities.makeCompactGrid(p3,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        // Add Variable edits
		int count = 0;
		for(Variable x : ((VNode) elem).getVariable()){
			addEdit(elem, x);
			count++;
		}
		
        pack(4+count, 1);
        
        
        ((UpdateVClassOccurence) command).setLower(((VClassOccurrence) elem).getInstanceMultiplicity().getLower());
    	((UpdateVClassOccurence) command).setUpper(((VClassOccurrence) elem).getInstanceMultiplicity().getUpper());
    	
    	textField2.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));
    	textField3.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));
        
        textField2.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                try {
                	String text = textField2.getText().trim();
                	Integer i = Integer.parseInt(text);
                	if(i < 0) return;
                    ((UpdateVClassOccurence) command).setLower(i);
                } catch (java.lang.NumberFormatException nfe) {
                }
            }
            public void removeUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
            public void changedUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
        });
        
        textField3.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                try {
                	String text = textField3.getText().trim();
                	Integer i;
                	if(text.equals("*")) i = -1;
                	else i = Integer.parseInt(text);
                	((UpdateVClassOccurence) command).setUpper(i);
                } catch (java.lang.NumberFormatException nfe) {
                }
            }
            public void removeUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
            public void changedUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
        });
        
	}
	
	private void addEdit(VSpec elem, final Variable v) {
	    // Part 1
        JPanel panel = new JPanel(new SpringLayout());
        panel.setBorder(null);
        panel.setOpaque(false);
        
        // Name
        final JTextField name = new JTextField(10);
        //name.setUI(new HudTextFieldUI());
        name.setText(v.getName());
        
        // Type
        JLabel l = new JLabel(((PrimitveType)v.getType()).getType().getName(), JLabel.TRAILING);
        //l.setUI(new HudLabelUI());
        
        final JComboBox types = new JComboBox();
        int index = 0;
        for(PrimitiveTypeEnum x : PrimitiveTypeEnum.VALUES){
        	types.addItem(x.getName());
        	if(x.getName().equals(((PrimitveType)v.getType()).getType().getName())){
        		types.setSelectedIndex(index);
        	}
        	index++;
        }
        
        // Add
        panel.add(name);
        panel.add(types);
        
        
        
        // Done
        top.add(panel);
        SpringUtilities.makeCompactGrid(panel,
                1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        //Part 2
        ((UpdateVClassOccurence)command).setVariable(v, v.getName(), ((PrimitveType)v.getType()).getType().getName());
        
        name.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));
        types.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));
        
    	// Part 3:
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
            	((UpdateVClassOccurence) command).setVariable(v, name.getText(), types.getSelectedItem().toString());
            }
            public void removeUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
            public void changedUpdate(DocumentEvent e) {
            	insertUpdate(e);
            }
        };
        name.getDocument().addDocumentListener(dl);
        types.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
            	((UpdateVClassOccurence) command).setVariable(v, name.getText(), types.getSelectedItem().toString());
            }
        });
	}
}
