/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.disneycompose.ui.main

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.disneycompose.extensions.viewModelIOContext
import com.skydoves.disneycompose.model.Poster
import com.skydoves.disneycompose.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val mainRepository: MainRepository
) : ViewModel() {

  private val _posterList: MutableLiveData<Boolean> = MutableLiveData(true)
  val posterList: LiveData<List<Poster>> = _posterList.switchMap {
    this.mainRepository.loadDisneyPosters(
      onStart = { _isLoading.postValue(true) },
      onSuccess = { _isLoading.postValue(false) },
      onError = { _toast.postValue(it) }
    ).asLiveData(viewModelIOContext)
  }

  private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
  val isLoading: LiveData<Boolean> get() = _isLoading

  private val _selectedTab: MutableState<Int> = mutableStateOf(0)
  val selectedTab: State<Int> get() = _selectedTab

  private val _toast: MutableLiveData<String> = MutableLiveData()
  val toast: LiveData<String> get() = _toast

  init {
    Timber.d("injection MainViewModel")
  }

  @MainThread
  fun selectTab(@StringRes tab: Int) {
    _selectedTab.value = tab
  }
}
