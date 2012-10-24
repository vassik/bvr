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

package no.sintef.cvl.ui.framework.elements;

import java.awt.Color;
import javax.swing.SwingConstants;

import no.sintef.cvl.ui.framework.ErrorHighlightableElement;
import no.sintef.cvl.ui.framework.SelectElement;
import no.sintef.cvl.ui.framework.ThreePartRoundedPanel;
import no.sintef.cvl.ui.framework.TitledElement;

import org.jdesktop.swingx.JXTitledSeparator;

public class ChoicePanel extends ThreePartRoundedPanel implements SelectElement, TitledElement, ErrorHighlightableElement {

    JXTitledSeparator titlebar = new JXTitledSeparator();
    JXTitledSeparator separatorbar = new JXTitledSeparator();
    JXTitledSeparator typebar = new JXTitledSeparator();

    public ChoicePanel() {
        titlebar.setForeground(Color.BLACK);
        titlebar.setHorizontalAlignment(SwingConstants.CENTER);
        separatorbar.setForeground(Color.BLACK);
        separatorbar.setTitle("");
        separatorbar.setHorizontalAlignment(SwingConstants.CENTER);
        separatorbar.setVisible(false);
        typebar.setForeground(Color.BLACK);
        typebar.setTitle("");
        typebar.setHorizontalAlignment(SwingConstants.CENTER);
        typebar.setVisible(false);

        addCenter(titlebar);
        addCenter(separatorbar);
        addCenter(typebar);

        setState(STATE.NO_ERROR);
    }

    @Override
    public void setTitle(String title) {
        titlebar.setTitle(title);
        this.setToolTipText("Component "+title);
    }

    public void setTypeName(String title) {
        typebar.setTitle(title);
        separatorbar.setVisible(true);
        typebar.setVisible(true);
    }

    private Boolean selected = false;

    public void setSelected(Boolean _selected) {
        selected = _selected;
        active = _selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    private STATE _state = STATE.NO_ERROR;

    public void setState(STATE state) {
        _state = state;
        if (_state.equals(STATE.IN_ERROR)) {
            this.setBackground(new Color(239, 50, 50, 150));
        } else {
            this.setBackground(new Color(0, 0, 0, 5));
        }
    }

    public STATE getCurrentState() {
        return _state;
    }

}
