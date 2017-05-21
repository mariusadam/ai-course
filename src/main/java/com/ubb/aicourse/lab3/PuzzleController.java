package com.ubb.aicourse.lab3;

import com.ubb.aicourse.lab3.aco.AntColonyOptimisation;
import com.ubb.aicourse.lab3.search.local.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marius Adam
 */
public class PuzzleController implements Initializable {
    public Tab                                   runTab;
    public TextArea                              randomizedListTextArea;
    public TextArea                              wordsListTextArea;
    public TextArea                              puzzleTextArea;
    public TextArea                              solutionTextArea;
    public TextField                             mutationProbField;
    public ComboBox<SelectionType>               selectionCBox;
    public ComboBox<AlgoType>                    algoCBox;
    public ComboBox<MutationType>                mutationCBox;
    public TextField                             maxIterationsField;
    public TextField                             maxTimeField;
    public TextField                             tournamentSizeField;
    public ComboBox<Integer>                     dataSetsCBox;
    public TextField                             initialPopField;
    public TableColumn<MetricsTableRow, Double>  fitnessColumn;
    public TableColumn<MetricsTableRow, Long>    iterationsColumn;
    public TableColumn<MetricsTableRow, Double>  totalTimeColumn;
    public TableColumn<MetricsTableRow, Double>  foundAfterColumn;
    public TableColumn<MetricsTableRow, Long>    foundItColumn;
    public TableColumn<MetricsTableRow, Integer> popColumn;
    public TableColumn<MetricsTableRow, String>  algoColumn;
    public TableView<MetricsTableRow>            metricsTable;
    public GridPane                              acoParams;
    public TextField                             antsCountField;
    public TextField                             decayFactorField;
    public TextField                             acoIterationsField;
    public TextField                             heuristicCoeffField;
    public TextField                             historyCoeffField;
    public TextField                             pheromoneRewardField;
    public GridPane                              evolutiveParams;

    private ObservableList<MetricsTableRow> metricsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        metricsList = FXCollections.observableArrayList();
        metricsTable.setItems(metricsList);
        algoColumn.setCellValueFactory(new PropertyValueFactory<>("algorithm"));
        fitnessColumn.setCellValueFactory(new PropertyValueFactory<>("bestFitness"));
        popColumn.setCellValueFactory(new PropertyValueFactory<>("populationSize"));
        foundItColumn.setCellValueFactory(new PropertyValueFactory<>("bestIteration"));
        foundAfterColumn.setCellValueFactory(new PropertyValueFactory<>("bestTimeSeconds"));
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeconds"));
        iterationsColumn.setCellValueFactory(new PropertyValueFactory<>("iterationsCount"));

        ObservableList<Integer> dataSetsList = FXCollections.observableArrayList(1, 2);
        dataSetsCBox.setItems(dataSetsList);
        selectionCBox.setItems(FXCollections.observableArrayList(SelectionType.Random,
                                                                 SelectionType.Roulette,
                                                                 SelectionType.Tournament
        ));
        algoCBox.setItems(FXCollections.observableArrayList(AlgoType.Evolutive, AlgoType.AntColonyOptimization));
        evolutiveParams.setVisible(true);
        acoParams.setVisible(false);
        mutationCBox.setItems(FXCollections.observableArrayList(MutationType.ModuloRandomResetting));

        // default setup for evolutive algo
        mutationCBox.getSelectionModel().select(MutationType.ModuloRandomResetting);
        algoCBox.getSelectionModel().select(AlgoType.Evolutive);
        selectionCBox.getSelectionModel().select(SelectionType.Tournament);
        mutationProbField.setText(Double.toString(0.7));
        maxIterationsField.setText(Integer.toString(1000));
        maxTimeField.setText(Integer.toString(0));
        tournamentSizeField.setText(Integer.toString(50));
        initialPopField.setText(Integer.toString(150));

        //default setup for aco
        acoIterationsField.setText(Integer.toString(150));
        antsCountField.setText(Integer.toString(20));
        decayFactorField.setText(Double.toString(0.5));
        heuristicCoeffField.setText(Double.toString(0.8));
        historyCoeffField.setText(Double.toString(1.0));
        pheromoneRewardField.setText(Double.toString(1));
    }

    public void onStartSearchClick(ActionEvent actionEvent) {
        try {
            if (algoCBox.getSelectionModel().getSelectedItem() == AlgoType.Evolutive) {
                runEvolutive();
            } else {
                runAco();
            }
        } catch (Throwable t) {
            showError(t.getClass() + " " + t.getMessage());
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        showInfo("Not implemented yet");
    }

    public void onLoadDataClick(ActionEvent actionEvent) throws FileNotFoundException {
        if (dataSetsCBox.getSelectionModel().getSelectedItem() == null) {
            showError("Select a dataset first");
            return;
        }
        String dataSet = dataSetsCBox.getSelectionModel().getSelectedItem().toString();
        String dir = "/lab3/" + dataSet + "/";

        Scanner wordsScanner = new Scanner(getClass().getResourceAsStream(dir + "words.txt"));
        wordsListTextArea.clear();
        while (wordsScanner.hasNext()) {
            wordsListTextArea.appendText(wordsScanner.next() + System.lineSeparator());
        }

        Scanner puzzleScanner = new Scanner(getClass().getResourceAsStream(dir + "puzzle.txt"));
        puzzleTextArea.clear();
        while (puzzleScanner.hasNext()) {
            puzzleTextArea.appendText(puzzleScanner.next() + System.lineSeparator());
        }
    }

    public void onRandomizeClick(ActionEvent actionEvent) {
        if (wordsListTextArea.getText().isEmpty()) {
            showError("Add some words to the words list.");
            return;
        }

        List<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(wordsListTextArea.getText().split("\n")));
        words = words.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        randomizedListTextArea.clear();
        Collections.shuffle(words, new Random(System.nanoTime()));
        words.forEach(s -> randomizedListTextArea.appendText(s + System.lineSeparator()));
    }

    public void onAlgorithmChanged(ActionEvent actionEvent) {
        switch (algoCBox.getSelectionModel().getSelectedItem()) {

            case Evolutive:
                acoParams.setVisible(false);
                evolutiveParams.setVisible(true);
                break;
            case AntColonyOptimization:
                evolutiveParams.setVisible(false);
                acoParams.setVisible(true);
                break;
        }
    }

    protected void showError(String message) {
        show(Alert.AlertType.ERROR, "Error", message);
    }

    private void runAco() {
        List<String> givenWords = getGivenWords();
        List<Integer> blankSpacesLength = getBlankSpacesLength();
        validateInput(givenWords, blankSpacesLength);

        AntColonyOptimisation aco = new AntColonyOptimisation(givenWords.toArray(new String[0]),
                                                              blankSpacesLength.toArray(new Integer[0])
        );

        aco.setMaxIt(Integer.parseInt(acoIterationsField.getText()));
        aco.setAntsCount(Integer.parseInt(antsCountField.getText()));
        aco.setHeuristicCoeff(Double.parseDouble(heuristicCoeffField.getText()));
        aco.setHistoryCoeff(Double.parseDouble(historyCoeffField.getText()));
        aco.setPheromoneRewardFactor(Double.parseDouble(pheromoneRewardField.getText()));

        String[] sol = aco.search();

        solutionTextArea.clear();
        solutionTextArea.appendText("Solution found using " + algoCBox
                .getSelectionModel()
                .getSelectedItem() + System.lineSeparator());
        for (String word : sol) {
            solutionTextArea.appendText(word + System.lineSeparator());
        }
        solutionTextArea.appendText("Fitness " + aco
                .getMetrics()
                .get(Metrics.Keys.BestChromosomeFitness) + " found in iteration " + aco
                .getMetrics()
                .get(Metrics.Keys.BestChromosomeIteration));

        Metrics metrics = aco.getMetrics();
        MetricsTableRow mtr = new MetricsTableRow();
        mtr.setAlgorithm(AlgoType.AntColonyOptimization.toString());
        mtr.setBestFitness(metrics.getDouble(Metrics.Keys.BestChromosomeFitness));
        mtr.setBestTimeSeconds(metrics.getLong(Metrics.Keys.BestChromosomeTime));
        mtr.setIterationsCount(metrics.getLong(Metrics.Keys.TotalIterations));
        mtr.setTotalSeconds(metrics.getLong(Metrics.Keys.TotalExecutionTime));
        mtr.setPopulationSize(Integer.parseInt(antsCountField.getText()));
        mtr.setBestIteration(metrics.getLong(Metrics.Keys.BestChromosomeIteration));
        metricsList.add(mtr);

    }


    private List<Integer> getBlankSpacesLength() {
        String puzzle = puzzleTextArea.getText();
        List<Integer> blankWordsLen = new ArrayList<>();
        for (String s : puzzle.split("[x\n]")) {
            if (!s.isEmpty()) {
                blankWordsLen.add(s.length());
            }
        }

        return blankWordsLen;
    }

    private List<String> getGivenWords() {
        List<String> givenWords = new ArrayList<>();
        for (String s : randomizedListTextArea.getText().split("\n")) {
            if (!s.isEmpty()) {
                givenWords.add(s);
            }
        }

        return givenWords;
    }

    private void validateInput(List<String> words, List<Integer> blankSpacesLength) {
        if (blankSpacesLength.size() != words.size()) {
            showError("Number of blank spaces must be the same with the number of words");
        }
    }

    private void runEvolutive() {
        PuzzleAgo evolutiveAlgo = new PuzzleAgo();

        List<String> givenWords = getGivenWords();
        List<Integer> blankSpacesLength = getBlankSpacesLength();
        validateInput(givenWords, blankSpacesLength);

        double mutationProb = 0;
        if (!mutationProbField.getText().isEmpty()) {
            mutationProb = Double.parseDouble(mutationProbField.getText());
        }
        evolutiveAlgo.setMutationProbability(mutationProb);


        MutationType mutationType = mutationCBox.getSelectionModel().getSelectedItem();
        switch (mutationType) {

            case ModuloRandomResetting:
                evolutiveAlgo.setMutationFn(GaUtils.moduloRandomResetting(givenWords.size()));
                break;
        }

        evolutiveAlgo.setFitnessFn(GaUtils.puzzleFitness(blankSpacesLength, givenWords));
        evolutiveAlgo.setChromosomeLength(givenWords.size());
        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < givenWords.size(); i++) {
            permutation.add(i);
        }
        List<Chromosome<Integer>> initialPopulation = new ArrayList<>();
        Integer initialPopulationSize = Integer.parseInt(initialPopField.getText());

        for (int i = 0; i < initialPopulationSize; i++) {
            List<Integer> representation = new ArrayList<>(permutation);
            Collections.shuffle(representation, new Random(System.nanoTime()));
            initialPopulation.add(new PuzzleChromosome(GaUtils.getInversionSequenceOf(representation)));
        }
        evolutiveAlgo.setInitialPopulation(initialPopulation);

        SelectionType selectionType = selectionCBox.getSelectionModel().getSelectedItem();
        switch (selectionType) {

            case Random:
                evolutiveAlgo.setSelectionFn(GaUtils.randomSelection());
                break;
            case Tournament:
                int tournamentSize = Integer.parseInt(tournamentSizeField.getText());
                evolutiveAlgo.setSelectionFn(GaUtils.tournamentSelection(tournamentSize % initialPopulation.size()));
                break;
            case Roulette:
                evolutiveAlgo.setSelectionFn(GaUtils.rouletteWheelSelection());
                break;
        }

        evolutiveAlgo.setMaxIterations(Long.parseLong(maxIterationsField.getText()));
        evolutiveAlgo.setMaxTimeMilliseconds(Long.parseLong(maxTimeField.getText()));

        Chromosome<Integer> sol = evolutiveAlgo.search();
        List<Integer> solPerm = GaUtils.getPermutationOf(sol.getRepresentation());
        solutionTextArea.clear();
        solutionTextArea.appendText("Solution found using " + algoCBox
                .getSelectionModel()
                .getSelectedItem() + System.lineSeparator());
        for (int i = 0; i < sol.length(); i++) {
            solutionTextArea.appendText(givenWords.get(solPerm.get(i)));
            solutionTextArea.appendText(System.lineSeparator());
        }
        solutionTextArea.appendText("Fitness " + sol.getFitness() + " found in iteration " + evolutiveAlgo
                .getMetrics()
                .get(Metrics.Keys.BestChromosomeIteration));

        Metrics metrics = evolutiveAlgo.getMetrics();
        MetricsTableRow mtr = new MetricsTableRow();
        mtr.setAlgorithm(AlgoType.Evolutive.toString());
        mtr.setBestFitness(sol.getFitness());
        mtr.setBestTimeSeconds(metrics.getLong(Metrics.Keys.BestChromosomeTime));
        mtr.setIterationsCount(metrics.getLong(Metrics.Keys.TotalIterations));
        mtr.setTotalSeconds(metrics.getLong(Metrics.Keys.TotalExecutionTime));
        mtr.setPopulationSize(initialPopulation.size());
        mtr.setBestIteration(metrics.getLong(Metrics.Keys.BestChromosomeIteration));
        metricsList.add(mtr);
    }

    private void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText("Something is did not go as expected");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showInfo(String s) {
        show(Alert.AlertType.INFORMATION, "Info", s);
    }
}
