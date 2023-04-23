package gui;


import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class histogramController extends Application {
	int DATA_SIZE = 20;
	int data[] = new int[DATA_SIZE];
	int group[] = new int[10];
	static ArrayList<Double> grades;
	static String courseName;
	static String topicName;
	static String Date;
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		
		prepareData();
		groupData();

		Label labelInfo = new Label();
		labelInfo.setText("\t\t\t\t\t\t\t\t\t\t\t\t\tGrades Distribution \n Course: " + courseName + "\n topic: " + topicName
				+ "\t\tDate: "+Date);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setCategoryGap(0);
		barChart.setBarGap(0);
		
		xAxis.setLabel("Grades Range");
		yAxis.setLabel("#Students");
		

		XYChart.Series series1 = new XYChart.Series();
		
		series1.setName("Histogram");
		series1.getData().add(new XYChart.Data("0-10", group[0]));
		series1.getData().add(new XYChart.Data("10-20", group[1]));
		series1.getData().add(new XYChart.Data("20-30", group[2]));
		series1.getData().add(new XYChart.Data("30-40", group[3]));
		series1.getData().add(new XYChart.Data("40-50", group[4]));
		series1.getData().add(new XYChart.Data("50-60", group[5]));
		series1.getData().add(new XYChart.Data("60-70", group[6]));
		series1.getData().add(new XYChart.Data("70-80", group[7]));
		series1.getData().add(new XYChart.Data("80-90", group[8]));
		series1.getData().add(new XYChart.Data("90-100", group[9]));
		
		
		barChart.getData().addAll(series1);
		
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(labelInfo, barChart);

		StackPane root = new StackPane();
		root.getChildren().add(vBox);

		Scene scene = new Scene(root, 800, 450);
		
		primaryStage.setTitle("Histogram");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	// generate dummy random data
	private void prepareData() {

		Random random = new Random();
		for (int i = 0; i < DATA_SIZE; i++) {
			data[i] = random.nextInt(100);
		}
		
	}

	// count data population in groups
	private void groupData() {
		for (int i = 0; i < 10; i++) {
			group[i] = 0;
		}

		for(Double grade : grades) {
			if (grade<= 10) {
				group[0]++;
			} else if (grade <= 20) {
				group[1]++;
			} else if (grade <= 30) {
				group[2]++;
			} else if (grade <= 40) {
				group[3]++;
			} else if (grade <= 50) {
				group[4]++;
			} else if (grade<= 60) {
				group[5]++;
			} else if (grade <= 70) {
				group[6]++;
			} else if (grade <= 80) {
				group[7]++;
			} else if (grade <= 90) {
				group[8]++;
			} else if (grade<= 100) {
				group[9]++;
			}
		}	
		
		
	}

}
