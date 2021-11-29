package `in`.papaya.googleias

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo

class MainActivity : AppCompatActivity(), BillingProcessor.IBillingHandler {

    private lateinit var buyNow: Button

    private lateinit var bp: BillingProcessor
    private lateinit var purchaseInfo: PurchaseInfo
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buyNow = findViewById(R.id.buyNow)

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()

        if (!preferences.getBoolean("isPremium", false)){
            Toast.makeText(this, "Show ads", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "don't show ads", Toast.LENGTH_SHORT).show()
        }


        bp = BillingProcessor(
            this,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAknyN4onvALigi487je57FHHbgQZDm+IWd84nBf3ED50yUuwe4T+ZJ/eMPCe1rpsbxjY0IO+tiudorIjAoneVo4vizyfYXxuSjNFhD6d9Ozwy4+j2Zmoh7XSjnmXR2bDZxOd/oz+vr9jaO2UCrXoqx7KxFb9aB2zHARcHiOlk1BRhrSBfDhS8nJVwWI1BP5Z3ccSPLEWMbP5pt7PyuScPrs5JZS92LeyARcRnqPmjvcK0itO6fYISdoMFRv9466c1I4oa8HHtDShHPSN2o8ZPV8oESe9l+QEzUrrLnK6YvnPOdl2/jznHiuzJP9HeNRvsR83FsFem0tVJaf764Miq1wIDAQAB",
            this
        )
        bp.initialize()

        buyNow.setOnClickListener {
            bp.subscribe(this, "papayacoders")
        }
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        editor.putBoolean("isPremium", true)
        editor.apply()
        Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
    }

    override fun onPurchaseHistoryRestored() {
//        TODO("Not yet implemented")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        TODO("Not yet implemented")
    }

    override fun onBillingInitialized() {
//        TODO("Not yet implemented")

        bp.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
//                TODO("Not yet implemented")
            }

            override fun onPurchasesError() {
//                TODO("Not yet implemented")
            }

        })

        if (bp.getSubscriptionPurchaseInfo("papayacoders") != null) {

            purchaseInfo = bp.getSubscriptionPurchaseInfo("papayacoders")!!


            if (purchaseInfo != null) {
                if (purchaseInfo.purchaseData.autoRenewing) {
                    editor.putBoolean("isPremium", true)
                    editor.apply()
                    Toast.makeText(this, "Already subscribe", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putBoolean("isPremium", false)
                    editor.apply()
                    Toast.makeText(this, "Not subscribed", Toast.LENGTH_SHORT).show()
                }
            } else {

                editor.putBoolean("isPremium", false)
                editor.apply()
                Toast.makeText(this, "Expired", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        if (bp != null) {
            bp.release()
        }
        super.onDestroy()
    }
}















