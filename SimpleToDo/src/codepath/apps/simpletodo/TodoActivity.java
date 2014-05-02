package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TodoActivity extends Activity {

  ArrayList<String> items;
  ArrayAdapter<String> itemsAdapter;
  ListView lvItems;
  final int REQUEST_CODE = 0;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_todo);
    lvItems = (ListView)findViewById(R.id.lvItems);
    readItems();
    itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    lvItems.setAdapter(itemsAdapter);
    setupListViewListener();
  }
  
  private void setupListViewListener(){
    lvItems.setOnItemLongClickListener( new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> aView, View item, int pos, long id){
        items.remove(pos);
        itemsAdapter.notifyDataSetInvalidated();
        saveItems();
        return true;
      }
    });    
    
    lvItems.setOnItemClickListener( new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> aView, View item, int pos, long id){
        Intent editIntent = new Intent(TodoActivity.this, EditItemActivity.class);
        editIntent.putExtra("pos", pos);
        editIntent.putExtra("origText", ((TextView)item).getText());
        startActivityForResult(editIntent, REQUEST_CODE);
      }
    });
  }
  
  public void addTodoItem(View v){
    EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
    String txt = etNewItem.getText().toString();
    if(txt != null && txt.length() > 0){
      itemsAdapter.add(etNewItem.getText().toString());
      etNewItem.setText("");
    }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
       String editedText = data.getExtras().getString("editedText");
       int post = data.getExtras().getInt("pos");
       items.set(post, editedText);
       itemsAdapter.notifyDataSetInvalidated();
       saveItems();
    }
  } 
  
  @Override
  protected void onSaveInstanceState(Bundle outState){
    saveItems();
  }
  
  private void readItems(){
    File filesDir = getFilesDir();
    File todoFile = new File(filesDir, "todo.txt");
    
    if(!todoFile.exists())
      return;
    
    try{
      items = new ArrayList<String>(FileUtils.readLines(todoFile));
    }catch(IOException e){
      items = new ArrayList<String>();
      e.printStackTrace();
    }
  }
  
  private void saveItems(){
    File filesDir = getFilesDir();
    File todoFile = new File(filesDir, "todo.txt");
    try{
      FileUtils.writeLines(todoFile, items);
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
