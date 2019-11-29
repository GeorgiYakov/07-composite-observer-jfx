package ohm.softa.a07.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


import java.awt.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import retrofit2.*;

import ohm.softa.a07.model.Meal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ohm.softa.a07.api.OpenMensaAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainController implements Initializable {
	private static final Logger logger = LogManager.getLogger(OpenMensaAPI.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private OpenMensaAPI openMensaAPI;

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private ListView<String> mealsList;

	@FXML
	private ObservableList<String> obsList;

	@FXML
	private Button btnClose;

	@FXML
	private Checkbox checkVegetarian;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
				String today = sdf.format(new Date());

				Callback<Meal> mealCall = openMensaAPI.getMeals(today).enqueue();
				List<Meal> mealsToday =  openMensaAPI.getMeals(today).enqueue().body();
				// create a new (observable) list and tie it to the view
				ObservableList<String> list = FXCollections.observableArrayList("Hans", "Dampf");
				mealsList.setItems(list);
			}
		});

		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});


	}

	public MainController() {
		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);
	}
}
