/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.traceAnalysis.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.gui.util.AllSelectionBindingItemListener;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class PrintStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private static final String PROPERTY_KEY_IDENTIFIER = PrintStep.class.getSimpleName();
	private static final String PROPERTY_KEY_MESSAGE_TRACES = PROPERTY_KEY_IDENTIFIER + ".messageTraces";
	private static final String PROPERTY_KEY_EXECUTION_TRACES = PROPERTY_KEY_IDENTIFIER + ".executionTraces";
	private static final String PROPERTY_KEY_INVALID_EXECUTION_TRACES = PROPERTY_KEY_IDENTIFIER + ".invalidExecutionTraces";
	private static final String PROPERTY_KEY_SYSTEM_MODEL = PROPERTY_KEY_IDENTIFIER + ".systemModel";
	private static final String PROPERTY_KEY_DEPLOYMENT_EQUIVALENCE_CLASSES = PROPERTY_KEY_IDENTIFIER + ".deploymentEquivalenceClasses";
	private static final String PROPERTY_KEY_ASSEMBLY_EQUIVALENCE_CLASSES = PROPERTY_KEY_IDENTIFIER + ".assemblyEquivalenceClasses";

	private static final String MESSAGE_TRACES_TOOLTIP = "Textual message trace representations of valid traces are written to an output file.";
	private static final String EXECUTION_TRACES_TOOLTIP = "Textual execution trace representations of valid traces are written to an output file";
	private static final String INVALID_EXECUTION_TRACES_TOOLTIP = "Textual execution trace representations of invalid traces are written to an output file";
	private static final String SYSTEM_MODEL_TOOLTIP = "Writes a HTML representation of the system model reconstructed from the trace data to an output file.";
	private static final String DEPLOYMENT_EQUIVALENCE_CLASSES_TOOLTIP = "<html>Output an overview about the equivalence classes.<br>"
			+ "On deployment-level identical components who belong to different nodes are represented as different equivalence classes.</html>";
	private static final String ASSEMBLY_EQUIVALENCE_CLASSES_TOOLTIP = "<html>Output an overview about the equivalence classes.<br>"
			+ "On assembly-level identical components who even belong to different nodes are represented as the same equivalence class.</html>";

	private final JLabel infoLabel = new JLabel("In this step you choose prints to be generated by the trace analysis.");

	private final JCheckBox messageTraces = new JCheckBox("Message Traces");
	private final JCheckBox executionTraces = new JCheckBox("Execution Traces");
	private final JCheckBox invalidExecutionTraces = new JCheckBox("Invalid Execution Traces");
	private final JCheckBox systemModel = new JCheckBox("System Model");
	private final JCheckBox deploymentEquivalenceClasses = new JCheckBox("Deployment Equivalence Classes");
	private final JCheckBox assemblyEquivalenceClasses = new JCheckBox("Assembly Equivalence Classes");
	private final JCheckBox allPrints = new JCheckBox("Select All");
	private final JPanel expandingPanel = new JPanel();

	public PrintStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
		this.addToolTipsToComponents();
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.infoLabel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(5, 5, 0, 0);
		this.add(this.messageTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.executionTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.invalidExecutionTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.systemModel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentEquivalenceClasses, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyEquivalenceClasses, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(10, 5, 5, 5);
		this.add(this.allPrints, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	private void addLogicToComponents() {
		this.allPrints.addItemListener(new AllSelectionBindingItemListener(this.messageTraces, this.executionTraces, this.invalidExecutionTraces, this.systemModel,
				this.deploymentEquivalenceClasses, this.assemblyEquivalenceClasses));
	}

	private void addToolTipsToComponents() {
		this.messageTraces.setToolTipText(MESSAGE_TRACES_TOOLTIP);
		this.executionTraces.setToolTipText(EXECUTION_TRACES_TOOLTIP);
		this.invalidExecutionTraces.setToolTipText(INVALID_EXECUTION_TRACES_TOOLTIP);
		this.systemModel.setToolTipText(SYSTEM_MODEL_TOOLTIP);
		this.deploymentEquivalenceClasses.setToolTipText(DEPLOYMENT_EQUIVALENCE_CLASSES_TOOLTIP);
		this.assemblyEquivalenceClasses.setToolTipText(ASSEMBLY_EQUIVALENCE_CLASSES_TOOLTIP);
	}

	@Override
	public boolean isNextStepAllowed() {
		final boolean nothingSelected = !(this.messageTraces.isSelected() || this.executionTraces.isSelected() || this.invalidExecutionTraces.isSelected()
				|| this.systemModel.isSelected() || this.deploymentEquivalenceClasses.isSelected() || this.assemblyEquivalenceClasses.isSelected());
		if (nothingSelected) {
			final int result = JOptionPane.showConfirmDialog(this, "No prints have been selected. Continue?", "No Prints Selected", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return (JOptionPane.YES_OPTION == result);
		}
		return true;
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.messageTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES);
		}

		if (this.executionTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES);
		}

		if (this.invalidExecutionTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES);
		}

		if (this.systemModel.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL);
		}

		if (this.deploymentEquivalenceClasses.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT);
		}
		if (this.assemblyEquivalenceClasses.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT);
		}
	}

	@Override
	public void loadDefaultConfiguration() {
		this.systemModel.setSelected(true);
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_MESSAGE_TRACES, Boolean.toString(this.messageTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_EXECUTION_TRACES, Boolean.toString(this.executionTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_INVALID_EXECUTION_TRACES, Boolean.toString(this.invalidExecutionTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_SYSTEM_MODEL, Boolean.toString(this.systemModel.isSelected()));
		properties.setProperty(PROPERTY_KEY_DEPLOYMENT_EQUIVALENCE_CLASSES, Boolean.toString(this.deploymentEquivalenceClasses.isSelected()));
		properties.setProperty(PROPERTY_KEY_ASSEMBLY_EQUIVALENCE_CLASSES, Boolean.toString(this.assemblyEquivalenceClasses.isSelected()));
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.messageTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_MESSAGE_TRACES)));
		this.executionTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_EXECUTION_TRACES)));
		this.invalidExecutionTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_INVALID_EXECUTION_TRACES)));
		this.systemModel.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_SYSTEM_MODEL)));
		this.deploymentEquivalenceClasses.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_DEPLOYMENT_EQUIVALENCE_CLASSES)));
		this.assemblyEquivalenceClasses.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_ASSEMBLY_EQUIVALENCE_CLASSES)));
	}

}
