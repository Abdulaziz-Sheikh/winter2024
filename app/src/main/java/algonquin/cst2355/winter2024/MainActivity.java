package algonquin.cst2355.winter2024;

import static algonquin.cst2355.winter2024.R.*;
import static algonquin.cst2355.winter2024.R.id.textView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public TextView tv = null;

    /*
     * onCreate is Equivalent of main method
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        EditText et = findViewById(id.edit);

        TextView tv = findViewById(id.textView);
        Button btn = findViewById(id.login);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            checkPasswordComplexity(password);
        });
    }
    /**
     * This boolean function validates if a special character is contained
     * within the password and returns true if valid or false if otherwise
     * @param c the object representing the character check
     * @return true or false
     */
    private boolean isSpecialCharacter(char c) {
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default: return false;
        }

    }

    /**
     * This method validates the password complexity from the edittext and returns
     * the validation result successfull if password checksout or the missing complexity
     * such as special chars or upper/lower cases.
     *
     * @param password the password value from the EditText field that is being checked.
     * @return returns true if validation passes or false if it does not.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = false;
        foundLowerCase = foundNumber = foundSpecial = false;
        TextView tv = findViewById(textView);
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            }else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }

        }
        if (!foundUpperCase){
            Toast.makeText(getApplicationContext(), "Your password must contain an upper case letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }else if(!foundLowerCase){
            Toast.makeText(getApplicationContext(), "Your password must contain a lower case letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }else if(!foundNumber){
            Toast.makeText(getApplicationContext(), "Your password must contain a number", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }else if(!foundSpecial){
            Toast.makeText(getApplicationContext(), "Your password must contain a special character", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        } else {
            tv.setText("Your password meets the requirements");
            return true;
        }

    }

}





