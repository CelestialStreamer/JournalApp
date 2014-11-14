package controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.transform.TransformerFactoryConfigurationError;

import journal.Entry;
import journal.Journal;
import journal.JournalEvent;
import journal.JournalEventListener;

/**
 * Controls everything
 * 
 * @author Brian Woodruff
 */
public class MainController {
  private Tab currentOpenTab = new Tab();
  private TextArea currentTextArea = new TextArea();
  private TreeItem<EntryTreeItem> entryRoot = new TreeItem<EntryTreeItem>(new EntryTreeItem(
      new Entry("", "Entries")));

  private File file;
  private Journal journal;
  private TreeItem<EntryTreeItem> journalRoot = new TreeItem<EntryTreeItem>(new EntryTreeItem(
      new Entry("", "Journal")));

  private List<EntryTreeItem> openTabs = new ArrayList<EntryTreeItem>();
  private TreeItem<EntryTreeItem> scriptureRoot = new TreeItem<EntryTreeItem>(new EntryTreeItem(
      new Entry("", "Scriptures")));

  @FXML
  private TextField searchField;
  private TreeItem<EntryTreeItem> searchRoot = new TreeItem<EntryTreeItem>(new EntryTreeItem(
      new Entry("", "Search Results")));
  @FXML
  private TabPane tabPane;

  private TreeItem<EntryTreeItem> topicRoot = new TreeItem<EntryTreeItem>(new EntryTreeItem(
      new Entry("", "Topics")));
  @FXML
  private TreeView<EntryTreeItem> tree;

  private void addTab(EntryTreeItem entry) {
    // Create text area and populate with text
    TextArea area = new TextArea(entry.getEntry().getContent());
    area.setWrapText(true);
    area.textProperty().addListener((e) -> {
      entry.getEntry().setContent(area.getText());
      updateTree();
    });

    // Create a tab with text as content, closable and set action listener
    Tab entryTab = new Tab(entry.toString());

    ContextMenu rootContextMenu = new ContextMenu();
    MenuItem closeMenuItem = new MenuItem("Close");
    rootContextMenu.getItems().add(closeMenuItem);

    closeMenuItem.setOnAction((e) -> {
      openTabs.remove(entry);
      tabPane.getTabs().remove(entryTab);
    });
    entryTab.setContextMenu(rootContextMenu);

    entryTab.setClosable(true);
    entryTab.setContent(area);
    entryTab.setOnSelectionChanged((e) -> {
      if (entryTab.isSelected()) {
        currentTextArea = area;
        currentOpenTab = entryTab;
      }
    });
    entryTab.setOnClosed((e) -> {
      openTabs.remove(entry);
    });

    // Add tab and update openTabs list
    tabPane.getTabs().add(entryTab);
    openTabs.add(entry);

    // Select last tab and put focus on text area
    tabPane.getSelectionModel().selectLast();
    area.requestFocus();
  }

  /**
   * First function called. Tabs, view trees, text fields and other properties
   * set.
   */
  @SuppressWarnings("unchecked")
  public void initialize() {
    try {
      journal = new Journal();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    tree.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          EntryTreeItem value = tree.selectionModelProperty().get().getSelectedItem().getValue();
          if (value == null || openTabs.contains(value)
              || !tree.selectionModelProperty().get().getSelectedItem().isLeaf()) {
            return;
          }
          addTab(value);
        }
      }
    });

    journalRoot.getChildren().addAll(entryRoot, topicRoot, scriptureRoot);
    journalRoot.setExpanded(true);

    tree.setRoot(journalRoot);

    searchField.textProperty().addListener((e) -> {
      if (searchField.getText().length() == 0) {
        tree.setRoot(journalRoot);
        return;
      }
      searchRoot.getChildren().clear();
      for (Entry entry : journal.find(searchField.getText())) {
        searchRoot.getChildren().add(new TreeItem<EntryTreeItem>(new EntryTreeItem(entry)));
      }
      searchRoot.setExpanded(true);
      tree.setRoot(searchRoot);
    });
    searchField.focusedProperty().addListener((e) -> {
      if (searchField.isFocused() && searchField.getText().length() != 0) {
        tree.setRoot(searchRoot);
      } else {
        tree.setRoot(journalRoot);
      }
    });

    searchField.setDisable(true);
  }

  /**
   * File->About
   * 
   * @param event
   */
  @FXML
  public void MenuAbout() {
    showDialogMessage("Journal Application by\nBrian Woodruff");
  }

  /**
   * File->Close Closes current tab.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  private void MenuClose() {
    openTabs.remove(currentOpenTab);
    tabPane.getTabs().remove(currentOpenTab);
  }

  /**
   * Edit->Copy
   */
  @FXML
  private void MenuCopy() {
    currentTextArea.copy();
  }

  /**
   * Edit->Cut
   */
  @FXML
  private void MenuCut() {
    currentTextArea.cut();
  }

  /**
   * Edit->Delete
   */
  @FXML
  private void MenuDelete() {
    currentTextArea.replaceSelection("");
  }

  /**
   * File->Exit Closes application.
   * 
   * @param event
   */
  @FXML
  private void MenuExit() {
    System.out.println("Exit!");
  }

  /**
   * File->Export Open an export dialog window.
   * 
   * @param event
   */
  @FXML
  private void MenuExport() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Export");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

    file = fileChooser.showSaveDialog(tabPane.getScene().getWindow());

    if (file != null) {
      try {
        journal.exportTxtFile(file);
      } catch (IOException e) {
        showDialogMessage("Error exporting file \"" + file.getAbsolutePath() + "\":\n"
            + e.getLocalizedMessage());
      }
    }
  }

  /**
   * File->Import Open an import dialog window.
   * 
   * @param event
   */
  @FXML
  private void MenuImport() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Import");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

    file = fileChooser.showOpenDialog(tabPane.getScene().getWindow());

    if (file != null) {
      try {
        ImportingDialogController controller = new ImportingDialogController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
            "/view/ImportingDialogView.fxml"));
        fxmlLoader.setController(controller);
        fxmlLoader.load();

        Scene scene = new Scene(fxmlLoader.getRoot());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Importing...");
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);

        journal.addJournalListener(new JournalEventListener() {
          @Override
          public void updateLoadStatus(JournalEvent event) {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                controller.setEntries(event.getEntries());
                controller.setScriptures(event.getScriptures());
                controller.setTopics(event.getTopics());
              }
            });
          }
        });

        stage.show();

        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              journal.importTxtFile(file);
              importJournal(journal);
            } catch (IOException e) {
              showDialogMessage("Error importing file \"" + file.getAbsolutePath() + "\":\n"
                  + e.getLocalizedMessage());
            }
          }
        }).start();
      } catch (IOException e) {
        showDialogMessage("Error importing file \"" + file.getAbsolutePath() + "\":\n"
            + e.getLocalizedMessage());
      }
    }
  }

  /**
   * File->New Creates a new journal entry.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  private void MenuNew() {
    searchField.setDisable(false);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
    Calendar cal = Calendar.getInstance();

    Entry entry = new Entry("", dateFormat.format(cal.getTime()));
    journal.addEntry(entry);

    addTab(new EntryTreeItem(entry));

    entryRoot.getChildren().add(new TreeItem<EntryTreeItem>(new EntryTreeItem(entry)));
    entryRoot.setExpanded(true);
  }

  /**
   * File->Open File Basic open file window.
   * 
   * @param event
   */
  @FXML
  private void MenuOpenFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));

    file = fileChooser.showOpenDialog(tabPane.getScene().getWindow());

    if (file != null) {
      open(file);
    }
  }

  /**
   * Edit->Paste
   */
  @FXML
  private void MenuPaste() {
    currentTextArea.paste();
  }

  /**
   * File->Save Saves silently if a save has already been used, otherwise opens
   * a Save As dialog window.
   * 
   * @param event
   */
  @FXML
  private void MenuSave() {
    if (file == null) {
      MenuSaveAs();
    } else {
      save(file);
    }
  }

  /**
   * File->Save As Basic save as dialog window.
   * 
   * @param event
   */
  @FXML
  private void MenuSaveAs() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save As");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    File file = fileChooser.showSaveDialog(tabPane.getScene().getWindow());

    if (file != null) {
      if (!file.getPath().endsWith(".xml")) {
        file = new File(file.getPath() + ".xml");
      }
      save(file);
    }
  }

  /**
   * Edit->Select All
   */
  @FXML
  private void MenuSelectAll() {
    currentTextArea.selectAll();
  }

  /**
   * Calls the open function for journal. Updates entry view tree. Opens a tab
   * for each entry. Sets focus to text field in last tab.
   * 
   * @param file
   */
  private void open(File file) {
    try {
      journal.open(file);
      importJournal(journal);
    } catch (Exception e) {
      showDialogMessage("Error reading file" + file.getAbsolutePath() + ":\n"
          + e.getLocalizedMessage());
    }
  }

  private void importJournal(Journal journal) {
    tabPane.getTabs().clear();
    openTabs.clear();

    entryRoot.getChildren().clear();
    scriptureRoot.getChildren().clear();
    topicRoot.getChildren().clear();

    for (Entry entry : journal.getEntries()) {
      entryRoot.getChildren().add(new TreeItem<EntryTreeItem>(new EntryTreeItem(entry)));
    }
    entryRoot.setExpanded(true);

    updateTree();

    int tabs = tabPane.getTabs().size();
    if (tabs != 0) {
      tabPane.getTabs().get(tabs - 1).getContent().requestFocus();
    }
  }

  /**
   * Calls the save function for journal
   * 
   * @param file
   */
  private void save(File file) {
    try {
      journal.save(file);
    } catch (Exception e) {
      showDialogMessage("Error saving file:\n" + e.getLocalizedMessage());
    } catch (TransformerFactoryConfigurationError e) {
      showDialogMessage("Error saving file:\n" + e.getLocalizedMessage());
    }
  }

  /**
   * Show a dialog window that displays a message.
   * 
   * @param message
   */
  private void showDialogMessage(String message) {
    DialogWindow.showDialog(message, tabPane.getScene().getWindow());
  }

  /**
   * Go through scripture references and topics and add to tree if not already
   * included.
   */
  private void updateTree() {
    scriptureRoot.getChildren().clear();
    topicRoot.getChildren().clear();

    // Scriptures
    for (java.util.Map.Entry<String, ArrayList<Entry>> pair : journal.getScriptureReferences()
        .entrySet()) {
      TreeItem<EntryTreeItem> book = new TreeItem<EntryTreeItem>(new EntryTreeItem(new Entry("",
          pair.getKey())));
      for (Entry anEntry : pair.getValue()) {
        book.getChildren().add(new TreeItem<EntryTreeItem>(new EntryTreeItem(anEntry)));
      }
      scriptureRoot.getChildren().add(book);
    }

    // Topics
    for (java.util.Map.Entry<String, ArrayList<Entry>> pair : journal.getTopicReferences()
        .entrySet()) {
      TreeItem<EntryTreeItem> book = new TreeItem<EntryTreeItem>(new EntryTreeItem(new Entry("",
          pair.getKey())));
      for (Entry anEntry : pair.getValue()) {
        book.getChildren().add(new TreeItem<EntryTreeItem>(new EntryTreeItem(anEntry)));
      }
      topicRoot.getChildren().add(book);
    }
    entryRoot.setExpanded(true);
  }
}
