/*******************************************************************************
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June
 * 2007; you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package no.sintef.bvr.tool.ui.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import no.sintef.bvr.tool.common.Constants;
import no.sintef.bvr.tool.controller.command.UpdateChoice;
import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.controller.command.Command;
import no.sintef.bvr.tool.ui.editor.BVRUIKernel;
import no.sintef.bvr.ui.framework.elements.CustomTextField;
import bvr.CompoundNode;
import bvr.PrimitiveTypeEnum;
import bvr.PrimitveType;
import bvr.VSpec;
import bvr.Variable;

public class ChoicePropertyEditor extends ElementPropertyEditor {

	private static final long serialVersionUID = -561022693337041081L;

	@SuppressWarnings("unchecked")
	public ChoicePropertyEditor(BVRUIKernel kernel, Command okCommand, VSpec elem, JComponent node, BVRNotifiableController controller) {
		super(kernel, okCommand, elem, node, controller);
		String name = (elem.getTarget() != null) ? elem.getTarget().getName() : Constants.NULL_TARGET;
		textNameField.setText(name);

		// Comment
		JPanel p = new JPanel(new SpringLayout());
		p.setBorder(null);
		p.setOpaque(false);

		JLabel l = new JLabel("Comment", JLabel.TRAILING);

		p.add(l);
		CustomTextField comment = new CustomTextField(15);

		l.setLabelFor(comment);
		p.add(comment);
		String setComment = controller.getVSpecControllerInterface().getNodesCommentText(node);
		comment.setText(setComment);
		((UpdateChoice) command).setComment(setComment);

		top.add(p);
		SpringUtilities.makeCompactGrid(p, 1, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		comment.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));

		comment.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					((UpdateChoice) command).setComment(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (BadLocationException ex) {
					// Logger.getLogger(NamedElementPropertyEditor.class.getName()).log(Level.SEVERE,
					// null, ex);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				insertUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				insertUpdate(e);
			}
		});

		// Vars
		int count = 2;
		for (Variable x : ((CompoundNode) elem).getVariable()) {
			addEdit(elem, x);
			count++;
		}

		pack(count, 1);
	}

	private void addEdit(VSpec elem, final Variable v) {
		// Part 1
		JPanel panel = new JPanel(new SpringLayout());
		panel.setBorder(null);
		panel.setOpaque(false);

		// Name
		final CustomTextField name = new CustomTextField(10);
		name.setText(v.getName());

		// Type
		JLabel l = new JLabel(((PrimitveType) v.getType()).getType().getName(), JLabel.TRAILING);

		final JComboBox types = new JComboBox();
		int index = 0;
		for (PrimitiveTypeEnum x : PrimitiveTypeEnum.VALUES) {
			types.addItem(x.getName());
			if (x.getName().equals(((PrimitveType) v.getType()).getType().getName())) {
				types.setSelectedIndex(index);
			}
			index++;
		}

		// Add
		panel.add(name);
		panel.add(types);

		// Done
		top.add(panel);
		SpringUtilities.makeCompactGrid(panel, 1, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		// Part 2
		((UpdateChoice) command).setVariable(v, v.getName(), ((PrimitveType) v.getType()).getType().getName());

		name.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));
		types.addKeyListener(new EnterAccepter(command, kernel.getEditorPanel()));

		// Part 3:
		DocumentListener dl = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				((UpdateChoice) command).setVariable(v, name.getText(), types.getSelectedItem().toString());
				// System.out.println("Set " + v.getName() + " to " +
				// name.getText() + "," + types.getSelectedItem());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				insertUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				insertUpdate(e);
			}
		};
		name.getDocument().addDocumentListener(dl);
		types.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((UpdateChoice) command).setVariable(v, name.getText(), types.getSelectedItem().toString());
			}
		});
	}

}
