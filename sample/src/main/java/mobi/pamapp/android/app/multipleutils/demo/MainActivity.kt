package mobi.pamapp.android.app.multipleutils.demo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.refactor.lib.colordialog.PromptDialog
import mobi.pamapp.android.app.multipleutils.R
import mobi.pamapp.android.app.multipleutils.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShowDialog.setOnClickListener(View.OnClickListener {
            val dialog: PromptDialog = PromptDialog(this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                    .setTitleText("Demo Tittle")
                    .setContentText("This is a demo dialog")
                    .setPositiveListener("Yes", PromptDialog.OnButtonListener { dialog -> dialog.dismiss() })
                    .setNegativeListener("No", PromptDialog.OnButtonListener { dialog -> dialog.dismiss() })
                    .setPositiveDrawable(R.drawable.abc_btn_check_material)
                    .setContentTextAlignment(Gravity.CENTER)
                    .setPositiveTextColor(Color.RED)
                    .setButtonCloseVisible(false)
                    .setCancelable(false, false)
                    .showDialog()
        })

        binding.btnShowDialog.performClick()
    }
}
