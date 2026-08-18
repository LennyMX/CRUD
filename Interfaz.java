package com.example.crudd;

import com.example.crudd.conect.PersonaConect;
import com.example.crudd.conect.TelefonoConect;
import com.example.crudd.modelo.Persona;
import com.example.crudd.modelo.Telefono;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Interfaz extends Application {
    private TableView<Persona> tablaPersonas = new TableView<>();
    private TextField txtNombre = new TextField();
    private TextField txtDireccion = new TextField();
    private TableView<Telefono> tablaTelefonos = new TableView<>();
    private TextField txtTelefono = new TextField();
    private PersonaConect personaConect = new PersonaConect();
    private TelefonoConect telefonoConect = new TelefonoConect();

    @Override
    public void start(Stage stage) {
        TableColumn<Persona, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Persona, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);

        TableColumn<Persona, String> colDir = new TableColumn<>("Dirección");
        colDir.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colDir.setPrefWidth(200);

        tablaPersonas.getColumns().addAll(colId, colNombre, colDir);
        tablaPersonas.setPrefHeight(150);

        TableColumn<Telefono, Integer> colIdTel = new TableColumn<>("ID");
        colIdTel.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdTel.setPrefWidth(50);
        TableColumn<Telefono, String> colTel = new TableColumn<>("Teléfono");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTel.setPrefWidth(200);

        tablaTelefonos.getColumns().addAll(colIdTel, colTel);
        tablaTelefonos.setPrefHeight(100);

        Button btnAgregarP = new Button("Agregar");
        Button btnEliminarP = new Button("Eliminar");
        Button btnAgregarT = new Button("Agregar Tel");
        Button btnEliminarT = new Button("Eliminar Tel");
        txtNombre.setPromptText("Nombre");
        txtDireccion.setPromptText("Dirección");
        txtTelefono.setPromptText("Número");

        btnAgregarP.setOnAction(e -> agregarPersona());
        btnEliminarP.setOnAction(e -> eliminarPersona());
        btnAgregarT.setOnAction(e -> agregarTelefono());
        btnEliminarT.setOnAction(e -> eliminarTelefono());


        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) cargarTelefonos(newV.getId());
        });
        HBox controlesPersona = new HBox(10, txtNombre, txtDireccion, btnAgregarP, btnEliminarP);
        controlesPersona.setAlignment(Pos.CENTER);

        HBox controlesTelefono = new HBox(10, txtTelefono, btnAgregarT, btnEliminarT);
        controlesTelefono.setAlignment(Pos.CENTER);

        VBox root = new VBox(15,
                new Label("Personas"), tablaPersonas, controlesPersona,
                new Label("Teléfonos (Seleccione una persona)"), tablaTelefonos, controlesTelefono
        );
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");
        cargarPersonas();
        Scene scene = new Scene(root, 550, 500);
        stage.setTitle("AgendaDB");
        stage.setScene(scene);
        stage.show();
    }


    private void cargarPersonas() {
        try {
            tablaPersonas.setItems(FXCollections.observableArrayList(personaConect.obtenerTodos()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarTelefonos(int personaId) {
        try {
            tablaTelefonos.setItems(FXCollections.observableArrayList(telefonoConect.obtenerPorPersona(personaId)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void agregarPersona() {
        try {
            personaConect.insertar(new Persona(txtNombre.getText(), txtDireccion.getText()));
            cargarPersonas();
            txtNombre.clear(); txtDireccion.clear();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void eliminarPersona() {
        Persona p = tablaPersonas.getSelectionModel().getSelectedItem();
        if (p != null) {
            try {
                personaConect.eliminar(p.getId());
                cargarPersonas();
                tablaTelefonos.getItems().clear();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void agregarTelefono() {
        Persona p = tablaPersonas.getSelectionModel().getSelectedItem();
        if (p != null) {
            try {
                telefonoConect.insertar(new Telefono(p.getId(), txtTelefono.getText()));
                cargarTelefonos(p.getId());
                txtTelefono.clear();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void eliminarTelefono() {
        Telefono t = tablaTelefonos.getSelectionModel().getSelectedItem();
        Persona p = tablaPersonas.getSelectionModel().getSelectedItem();
        if (t != null && p != null) {
            try {
                telefonoConect.eliminar(t.getId());
                cargarTelefonos(p.getId());
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
