import com.example.playlist.ui.playActivity.viewModel.PlayTrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playViewModelModule = module {
    viewModel {
        PlayTrackViewModel()
    }
}