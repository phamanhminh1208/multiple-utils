package mobi.pamapp.android.app.multipleutils.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import cn.refactor.lib.colordialog.PromptDialog
import kotlinx.android.synthetic.main.activity_main.*
import mobi.pamapp.android.app.multipleutils.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShowDialog.setOnClickListener(View.OnClickListener {
            val dialog: PromptDialog = PromptDialog(this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                    .setTitleText("Demo Tittle")
                    .setContentText("This is a demo dialog")
                    .setPositiveListener("Yes", PromptDialog.OnButtonListener { dialog -> dialog.dismiss() })
                    .setNegativeListener("No", PromptDialog.OnButtonListener { dialog -> dialog.dismiss() })
                    .setPositiveDrawable(R.drawable.abc_btn_check_material)
                    .setContentTextAlignment(Gravity.CENTER)
                    .setPositiveTextColor(Color.RED)
                    .setButtonCloseVisible(true)
                    .setCancelable(false, false)
                    .showDialog()
        })

        btnShowDialog.performClick()
    }
}
