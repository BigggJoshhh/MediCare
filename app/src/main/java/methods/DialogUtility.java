package methods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class DialogUtility {

    public static void showAlertDialog(Context context, String title, String description, Class<?> targetActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(description)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, targetActivity);
                        context.startActivity(i);
                        if (context instanceof android.app.Activity) {
                            ((android.app.Activity) context).finish();
                        }
                        dialog.dismiss(); // Close the dialog
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to execute when the "Cancel" button is clicked
                        dialog.dismiss(); // Close the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
