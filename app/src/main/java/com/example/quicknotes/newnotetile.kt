import android.service.quicksettings.TileService
import androidx.glance.action.actionStartActivity
import androidx.navigation.NavHostController
import com.example.quicknotes.MainActivity

class newnotetile: TileService() {


    override fun onClick() {
        super.onClick()
        actionStartActivity<MainActivity>()

    }

}