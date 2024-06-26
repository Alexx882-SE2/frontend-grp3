package se2.group3.gameoflife.frontend.activities;

import static se2.group3.gameoflife.frontend.activities.MainActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity {

    private LobbyViewModel lobbyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyViewModel = new ViewModelProvider(this).get(LobbyViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.buttonReturnToUser).setOnClickListener(v -> {
            Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonCreateNewGame).setOnClickListener(v -> {
            String playerName = getIntent().getStringExtra("username");
            lobbyViewModel.getLobby().observe(LobbyActivity.this, lobbyDTO -> {
                Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                intent.putExtra("lobbyDTO", lobbyDTO);
                startActivity(intent);
            });
            Log.d(TAG, "Before create lobby!");
            assert playerName != null;
            lobbyViewModel.createLobby(playerName, getIntent().getStringExtra("uuid"));
            Log.d(TAG, "After create lobby!");
        });

        findViewById(R.id.buttonJoinGame).setOnClickListener(v -> {
            setContentView(R.layout.activity_join_game);
            findViewById(R.id.GObutton).setOnClickListener(v1 -> {
                String playerName = getIntent().getStringExtra("username");
                if(playerName == null) Log.e(TAG, "Error with the username intent!");
                EditText lobbyIDText = findViewById(R.id.lobbyCodeEntry);
                String lobbyIDString = lobbyIDText.getText().toString();
                if (!lobbyIDString.isEmpty()){
                    long lobbyID = Long.parseLong(lobbyIDString);
                    lobbyViewModel.getLobby().observe(LobbyActivity.this, lobbyDTO -> {
                        Intent intent = new Intent(LobbyActivity.this, StartGameActivity.class);
                        intent.putExtra("lobbyDTO", lobbyDTO);
                        startActivity(intent);
                    });
                    assert playerName != null;
                    lobbyViewModel.joinLobby(lobbyID, playerName);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        lobbyViewModel.dispose();
        super.onDestroy();
    }
}