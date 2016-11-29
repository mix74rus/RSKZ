package ru.fors.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import ru.fors.pages.LoginException;
import ru.fors.pages.User;

import java.io.File;
import java.io.FileNotFoundException;

public class MainPane extends GridPane {
    private final TextField usernameTextField = new TextField("");
    private final PasswordField passwordField = new PasswordField();
    private final TextField chromeDriverPath = new TextField("");
    private final TextField representationTextField = new TextField();
    private final TextArea activityTextArea = new TextArea();
    private final Label activityLabel = new LabelWithStyle("Активность:");
    private final CheckBox inWaitCheckBox = new CheckBox("Перевод в ожидание");
    private final CheckBox changeActivityCheckBox = new CheckBox("Изменение активности");
    private final RadioButton closeIncidentRadioButton = new RadioButton("Закрытие инцидентов");
    private final RadioButton workingWithActivitiesRadioButton = new RadioButton("Работа с инцидентами");
    /***************/
    private final CheckBox toReleaseCheckBox = new CheckBox("В релиз");
    private final CheckBox newCheckBox = new CheckBox("Новая фича");
    private final RadioButton returnRadioButton = new RadioButton("Возврат");
    private final TextArea returnTextArea = new TextArea();
    private final Label returnLabel = new LabelWithStyle("Причина возврата: ");
    private final RadioButton relevanceRequestRadioButton = new RadioButton("Запрос Актуальности");
    private final TextArea relevanceRequestTextArea = new TextArea();
    private final Label relevanceRequestLabel = new LabelWithStyle("Текст запроса:");
    /***************/
    private final TextArea solutionTextArea = new TextArea();
    private final Label closeLabel = new LabelWithStyle("Текст решения: ");
    private Text errorMessage;

    private final String RED_BORDER = "-fx-border-color: red";
    private final String INHERIT_BORDER = "-fx-border-color: inherit";

    private final int MAX_TEXT_LENGTH = 60;

    public MainPane() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));
        setVgap(10);
        setHgap(10);
        setColumns();

        fillFirstColumn();
        fillSecondColumn();
    }

    public MainPane(String path) {
        this();
        try {
            User.decode(path).startWork();
        } catch (LoginException e) {
            failToLoginMessage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void failToLoginMessage() {
        showError("Ошибка аутентификации");
    }

    private void driverNotFound() {
        showError("Не удалось запустить Chrome");
    }

    private void showError(String text){
        errorMessage = new Text(text);
        errorMessage.setFill(Color.RED);
        add(errorMessage, 0, (this.getChildren().size() - 1) / 2);
        setColumnSpan(errorMessage, 2);
        setHalignment(errorMessage, HPos.CENTER);
    }

    private void fillSecondColumn() {
        final int col = 1;
        int row = 0;
        toReleaseCheckBox.setVisible(false);
        add(usernameTextField, col, row++);

        add(passwordField, col, row++);

        HBox.setHgrow(chromeDriverPath, Priority.ALWAYS);
        chromeDriverPath.setDisable(true);

        final Button fileChooseButton = new Button("...");
        fileChooseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите путь к драйверу браузера Chrome");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Исполняемые файлы", "*.exe"),
                    new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
            if (selectedFile != null)
                chromeDriverPath.setText(selectedFile.getPath());
        });

        final HBox pathHBox = new HBox(chromeDriverPath, fileChooseButton);
        pathHBox.setSpacing(5);
        add(pathHBox, col, row++);

        add(representationTextField, col, row++);
//        add(closeIncidentRadioButton, col-1, row++);
        final HBox radioButtons = new HBox(workingWithActivitiesRadioButton, relevanceRequestRadioButton);
        radioButtons.setSpacing(20.);
        add(radioButtons, col, row++);
        workingWithActivitiesRadioButton.setOnAction(e -> {
            /***/
            //newCheckBox.setVisible(true);
            relevanceRequestLabel.setVisible(false);
            relevanceRequestTextArea.setVisible(false);
            representationTextField.setDisable(false);
            returnLabel.setVisible(false);
            returnTextArea.setVisible(false);
            /***/
            toReleaseCheckBox.setVisible(true);
            closeLabel.setVisible(false);
            solutionTextArea.setVisible(false);
            changeActivityCheckBox.setVisible(true);
            inWaitCheckBox.setVisible(true);
        });

        /***/
        representationTextField.setDisable(true);
        /***/
        changeActivityCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                activityLabel.setVisible(true);
                activityLabel.setManaged(true);
                activityTextArea.setVisible(true);
                activityTextArea.setManaged(true);
            } else {
                activityLabel.setVisible(false);
                activityLabel.setManaged(false);
                activityTextArea.setVisible(false);
                activityTextArea.setManaged(false);
            }
        });
        changeActivityCheckBox.setVisible(false);
        final HBox checkBoxHBox = new HBox(changeActivityCheckBox, newCheckBox);
        checkBoxHBox.setAlignment(Pos.CENTER);
        checkBoxHBox.setSpacing(15.);
        add(checkBoxHBox, col, row++);

        activityTextArea.setPrefRowCount(3);
        activityTextArea.setVisible(false);
        activityTextArea.setManaged(false);
        activityTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (activityTextArea.getText().length() > MAX_TEXT_LENGTH)
                activityTextArea.setText(activityTextArea.getText(0, MAX_TEXT_LENGTH));
        });
        add(activityTextArea, col, row);
    }

    private void fillFirstColumn() {
        final int col = 0;
        int row = 0;

        //passwordField.setText("123AAAb");
        relevanceRequestTextArea.setText("Добрый день!\n По результатам анализа информации, изложенной в обращении, просим сообщить ее актуальность.");
        final Label usernameLabel = new LabelWithStyle("Логин:");
        add(usernameLabel, col, row++);

        final Label passwordLabel = new LabelWithStyle("Пароль:");
        add(passwordLabel, col, row++);

        final Label pathLabel = new LabelWithStyle("Путь к драйверу браузера Chrome:");
        add(pathLabel, col, row++);

        final Label representationLabel = new LabelWithStyle("Имя представления:");
        add(representationLabel, col, row++);

        inWaitCheckBox.setVisible(false);
        closeLabel.setVisible(false);
        final HBox hBox = new HBox(toReleaseCheckBox, inWaitCheckBox);
        hBox.setSpacing(15.);
        hBox.setAlignment(Pos.CENTER);
        add(hBox, col, ++row);
        final HBox radioButtonsHBox = new HBox(closeIncidentRadioButton, returnRadioButton);
        radioButtonsHBox.setSpacing(20.);
        add(radioButtonsHBox,col,row - 1);
//        add(closeIncidentRadioButton, col, row - 1);
//        add(returnRadioButton, col + 2, row - 1);
        add(solutionTextArea, col + 1, row);
        /***/
        add(returnLabel, col, row);
        add(returnTextArea, col+1, row);
        add(relevanceRequestLabel, col, row);
        add(relevanceRequestTextArea, col+1, row);
        /***/
        add(closeLabel, col, row++);

        /***/
        returnLabel.setVisible(false);
        returnTextArea.setVisible(false);
        relevanceRequestLabel.setVisible(false);
        relevanceRequestTextArea.setVisible(false);
        newCheckBox.setVisible(false);
        /***/

        solutionTextArea.setVisible(false);
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(
                workingWithActivitiesRadioButton,
                closeIncidentRadioButton,
                returnRadioButton,
                relevanceRequestRadioButton
        );
        //DELETE THIS
        toggleGroup.selectToggle(returnRadioButton);
        returnLabel.setVisible(true);
        returnTextArea.setVisible(true);
        returnTextArea.setText("По указанной проблеме объявлена авария за номером IM100483113");
        /***/

        relevanceRequestRadioButton.setOnAction(e -> {
            newCheckBox.setVisible(false);
            toReleaseCheckBox.setVisible(false);
            relevanceRequestLabel.setVisible(true);
            relevanceRequestTextArea.setVisible(true);
            representationTextField.setText("");
            returnLabel.setVisible(false);
            returnTextArea.setVisible(false);
            representationTextField.setDisable(true);
            solutionTextArea.setVisible(false);
            closeLabel.setVisible(false);
            activityLabel.setVisible(false);
            activityTextArea.setVisible(false);
            activityTextArea.setManaged(false);
            changeActivityCheckBox.setVisible(false);
            inWaitCheckBox.setVisible(false);
            inWaitCheckBox.setSelected(false);
            changeActivityCheckBox.setSelected(false);

        });

        returnRadioButton.setAlignment(Pos.CENTER_RIGHT);
        returnRadioButton.setOnAction(e -> {
                    newCheckBox.setVisible(false);
            toReleaseCheckBox.setVisible(false);
            relevanceRequestLabel.setVisible(false);
            relevanceRequestTextArea.setVisible(false);
            representationTextField.setText("");
            returnLabel.setVisible(true);
            returnTextArea.setVisible(true);
            representationTextField.setDisable(true);
            solutionTextArea.setVisible(false);
            closeLabel.setVisible(false);
            activityLabel.setVisible(false);
            activityTextArea.setVisible(false);
            activityTextArea.setManaged(false);
            changeActivityCheckBox.setVisible(false);
            inWaitCheckBox.setVisible(false);
            inWaitCheckBox.setSelected(false);
            changeActivityCheckBox.setSelected(false);

        }
        /***/

        );

        closeIncidentRadioButton.setOnAction(e -> {
            toReleaseCheckBox.setVisible(false);
            /***/
            relevanceRequestLabel.setVisible(false);
            relevanceRequestTextArea.setVisible(false);
            representationTextField.setDisable(false);
            returnLabel.setVisible(false);
            returnTextArea.setVisible(false);
            newCheckBox.setVisible(false);
            /***/
            activityLabel.setVisible(false);
            activityTextArea.setVisible(false);
            activityTextArea.setManaged(false);
            changeActivityCheckBox.setVisible(false);
            inWaitCheckBox.setVisible(false);
            inWaitCheckBox.setSelected(false);
            changeActivityCheckBox.setSelected(false);
            solutionTextArea.setVisible(true);
            closeLabel.setVisible(true);
        });

        activityLabel.setVisible(false);
        activityLabel.setManaged(false);
        add(activityLabel, col, row++);

        final Button runButton = new Button("Запуск");
        add(runButton, col + 1, row);
        setColumnSpan(runButton, 2);
        setHalignment(runButton, HPos.RIGHT);
        runButton.setOnAction(e -> startButton());

        final Button saveButton = new Button("Сохранить");
        add(saveButton, col, row);
        setHalignment(saveButton, HPos.CENTER);
        saveButton.setOnAction(e -> saveButton());

        final Button openButton = new Button("Открыть");
        add(openButton, col, row);
        setHalignment(openButton, HPos.LEFT);
        openButton.setOnAction(e -> openButton());
    }

    private void openButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите путь файла конфигурации");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
        if (selectedFile != null)
            try {
                User user = User.decode(selectedFile.getPath());
                fillFields(user);
                user.startWork();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (LoginException e) {
                failToLoginMessage();
            } catch (IllegalStateException e){
                driverNotFound();
            }
    }

    private void fillFields(User user) {
        usernameTextField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        chromeDriverPath.setText(user.getDriverPath());
        representationTextField.setText(user.getRepresentation());
        if (user.isDoChangeActivity()) {
            changeActivityCheckBox.setSelected(true);
            activityTextArea.setText(user.getActivity());
            workingWithActivitiesRadioButton.fire();
        }
        if (user.isDoChangeStatus()) {
            inWaitCheckBox.setSelected(true);
            workingWithActivitiesRadioButton.fire();
        }
        if (user.isDoChangeStatusToSolve()) {
            solutionTextArea.setText(user.getSolution());
            closeIncidentRadioButton.fire();
        }
    }

    private void saveButton() {
        User user = getUser();
        if (user != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите путь для сохранения файла конфигурации");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML", "*.xml"),
                    new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            File selectedFile = fileChooser.showSaveDialog(getScene().getWindow());
            if (selectedFile != null)
                User.save(getUser(), selectedFile.getPath());
        }
    }

    private void startButton() {
        User user = getUser();
        if (user != null) {
            try {
                if(errorMessage != null){
                    errorMessage.setText("");
                }
                user.startWork();
            } catch (LoginException e) {
                failToLoginMessage();
            }
        }
    }

    private User getUser() {
        if (checkInputData()) {
            if (workingWithActivitiesRadioButton.isSelected())
                return startManagingIncidents();
            else if (closeIncidentRadioButton.isSelected())
                return startClosingIncidents();
            else if (returnRadioButton.isSelected())
                return startReturnIncedent();
            else if (relevanceRequestRadioButton.isSelected())
                return startRelevanceRequestProcess();
        }

        return null;
    }

    private User startRelevanceRequestProcess() {
        return User.getUserInstanceForRelevanceRequest(usernameTextField.getText(),passwordField.getText(),chromeDriverPath.getText(),relevanceRequestTextArea.getText());
    }

    private User startReturnIncedent() {
        return new User(usernameTextField.getText(),passwordField.getText(),chromeDriverPath.getText(), true,returnTextArea.getText());
    }

    private User startClosingIncidents() {
        if (checkField(solutionTextArea))
            return new User(usernameTextField.getText(), passwordField.getText(), chromeDriverPath.getText(),
                    representationTextField.getText(), solutionTextArea.getText());
        return null;
    }

    private User startManagingIncidents() {
        if (checkCheckBoxes()) {
            if(newCheckBox.isSelected()){
                return User.getUserInstanceForNewFeature(usernameTextField.getText(), passwordField.getText(),
                        chromeDriverPath.getText(), representationTextField.getText());
            }else if (toReleaseCheckBox.isSelected()) {
                return User.getUserInstanceForSendingToRelease(usernameTextField.getText(), passwordField.getText(),
                        chromeDriverPath.getText(), representationTextField.getText());
            } else if (changeActivityCheckBox.isSelected()) {
                return new User(usernameTextField.getText(), passwordField.getText(), chromeDriverPath.getText(),
                        representationTextField.getText(), activityTextArea.getText(),
                        inWaitCheckBox.isSelected());
            } else {
                return new User(usernameTextField.getText(), passwordField.getText(), chromeDriverPath.getText(),
                        representationTextField.getText());
            }
        }
        return null;
    }

    private boolean checkInputData() {
        return checkField(usernameTextField) &&
                checkField(passwordField) &&
                checkField(chromeDriverPath);
                //checkField(representationTextField);
    }

    private boolean checkField(TextInputControl field) {
        if (field.getText().isEmpty()) {
            field.setStyle(RED_BORDER);
            return false;
        } else {
            field.setStyle(INHERIT_BORDER);
            return true;
        }
    }

    private boolean checkCheckBoxes() {
        if (!inWaitCheckBox.isSelected()
                && !changeActivityCheckBox.isSelected()
                && !toReleaseCheckBox.isSelected()
                && !newCheckBox.isSelected()) {
            inWaitCheckBox.setStyle(RED_BORDER);
            changeActivityCheckBox.setStyle(RED_BORDER);
            toReleaseCheckBox.setStyle(RED_BORDER);
            newCheckBox.setStyle(RED_BORDER);
            return false;
        } else {
            inWaitCheckBox.setStyle(INHERIT_BORDER);
            changeActivityCheckBox.setStyle(INHERIT_BORDER);
            toReleaseCheckBox.setStyle(INHERIT_BORDER);
            newCheckBox.setStyle(INHERIT_BORDER);
            return true;
        }
    }

    private void setColumns() {
        ColumnConstraints labelsColumn = new ColumnConstraints();
        labelsColumn.setHalignment(HPos.RIGHT);
        labelsColumn.setPercentWidth(50);
        ColumnConstraints fieldsColumn = new ColumnConstraints();
        fieldsColumn.setHalignment(HPos.LEFT);
        fieldsColumn.setPercentWidth(50);
        getColumnConstraints().addAll(labelsColumn, fieldsColumn);
    }
}

class LabelWithStyle extends Label {
    LabelWithStyle(String text) {
        super(text);
        setWrapText(true);
        setTextAlignment(TextAlignment.RIGHT);
        setStyle("-fx-font-size: 15px");
    }
}