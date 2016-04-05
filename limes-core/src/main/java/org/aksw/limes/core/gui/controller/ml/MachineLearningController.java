package org.aksw.limes.core.gui.controller.ml;

import org.aksw.limes.core.gui.model.Config;
import org.aksw.limes.core.gui.model.ml.MachineLearningModel;
import org.aksw.limes.core.gui.view.ml.MachineLearningView;
import org.aksw.limes.core.ml.setting.LearningSetting;

public abstract class MachineLearningController {

	protected MachineLearningView mlView;

	protected MachineLearningModel mlModel;

	public MachineLearningView getMlView() {
		return mlView;
	}

	public void setMlView(MachineLearningView mlView) {
		this.mlView = mlView;
	}

	public void setMLAlgorithmToModel(String algorithmName) {
		this.mlModel.initializeData(algorithmName);
	}

	public void setParameters() {
		LearningSetting params = this.mlModel.getLearningsetting();
		params.setInquerySize(this.mlView.getInquiriesSpinner().getValue());
		params.setMaxDuration(this.mlView.getMaxDurationSpinner().getValue());
		params.setMaxIteration(this.mlView.getMaxIterationSpinner().getValue());
		params.setMaxQuality(this.mlView.getMaxQualitySlider().getValue());
		params.setTerminationCriteria(
				LearningSetting.TerminationCriteria.valueOf(this.mlView
						.getTerminationCriteriaSpinner().getValue()),
				this.mlView.getTerminationCriteriaValueSlider().getValue());
		params.setBeta(this.mlView.getBetaSlider().getValue());
		if (this.mlModel.getMlalgorithm().getName().equals("EAGLE")) {
			System.out.println(this.mlModel.getMlalgorithm().getName());
			params.setGenerations(this.mlView.getGenerationsSpinner()
					.getValue());
			params.setPopulation(this.mlView.getPopulationSpinner().getValue());
			params.setMutationRate((float) this.mlView.getMutationRateSlider()
					.getValue());
			params.setReproductionRate((float) this.mlView
					.getReproductionRateSlider().getValue());
			params.setCrossoverRate((float) this.mlView
					.getCrossoverRateSlider().getValue());
			params.setPreserveFittest(this.mlView.getPreserveFittestCheckBox()
					.isSelected());
			// TODO set measure
		} else if (this.mlModel.getMlalgorithm().getName().equals("LION")) {
			System.out.println(this.mlModel.getMlalgorithm().getName());
			params.setGammaScore(this.mlView.getGammaScoreSlider().getValue());
			params.setExpansionPenalty(this.mlView.getExpansionPenaltySlider()
					.getValue());
			params.setReward(this.mlView.getRewardSlider().getValue());
			params.setPrune(this.mlView.getPruneCheckBox().isSelected());
		}
		this.mlModel.setLearningsetting(params);
	}

	public void learn(MachineLearningView view) {
		this.mlModel.learn(view);
	}

}