package mailssage.android.com.mailssage.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.fragments.MessageListFragment;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 26/07/2015.
 */
public class MessageListActivity extends Activity
        implements MessageListFragment.CallBack
{

    private static final String LOGTAG = "MY_CONVER_DETAIL_ACTI";
    private static final int REQUEST_CODE = 10;
    private static MainActivity.MainScreenFragment currentMainScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_message_list);

        MessageListFragment listFragment = new MessageListFragment();
        Log.i(LOGTAG, "onCreate method has successfully created a message list fragment object.");

        Bundle conversationBundle = getIntent().getBundleExtra(Conversation.CONVERSATION_BUNDLE);
        currentMainScreenFragment = (MainActivity.MainScreenFragment) getIntent()
                                        .getSerializableExtra(MainActivity.CURRENT_MAIN_SCREEN_FRAGMENT);

        if (conversationBundle != null
                && conversationBundle.containsKey(Conversation.BUNDLE_CONVERSATION_ID)) {
            Log.i(LOGTAG, "Successfully received a conversation bundle.");
            listFragment.setArguments(conversationBundle);

            getFragmentManager().beginTransaction()
                    .add(R.id.conversation_detail_activity, listFragment)
                    .commit();
        } else {
            Log.i(LOGTAG, "Received a null or non-conversation bundle.");
        }

        //Listen to the enter key and send the message when is pressed
        EditText newMessageBox = (EditText) findViewById(R.id.new_message_input_box);
        if (newMessageBox != null) {
            Log.i(LOGTAG, "Found input box and start setting the listener");

            newMessageBox.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_ENTER:
                                return sendNewMessage();
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private boolean sendNewMessage(){
        EditText newMessageBox = (EditText) findViewById(R.id.new_message_input_box);
        String newMessageContent = newMessageBox.getText().toString();
        Log.i(LOGTAG, "Receive new message content: " + newMessageContent);

        newMessageBox.setText("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                Log.i(LOGTAG, "Cannot identify the selected menu item");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message_list, menu);

        return true;
    }

    @Override
    public void onMessageSelected(Message selectedMessage) {
        Log.i(LOGTAG, "Selected message: " + selectedMessage);

        Intent intent = new Intent(this, MessageDetailActivity.class);
        Bundle messageBundle = selectedMessage.toBundle();
        intent.putExtra(Message.MESSAGE_BUNDLE, messageBundle);

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public MainActivity.MainScreenFragment getCurrentMainScreenFragment() {
        return this.currentMainScreenFragment;
    }
}
