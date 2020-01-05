package common.observer;

import common.model.Music;

import java.util.List;

public interface IObserver {
    void Update(Object o);
    void OnLoginSuccess(Object ref, String token);
    void OnInvalidCredentials(Object ref);
    void OnRegisterSuccess(Object ref, String token);
    void OnPasswordsNotMatching(Object ref);
    void OnUsernameNotUnique(Object ref);
    void OnAddSongSuccess(Object ref, Music music);
    void OnReadyForUpload(Object ref, String hostname, Integer port);
    void OnReadyForDownload(Object ref, String hostname, Integer port);
    void OnGetSongsSuccess(Object ref, List<Music> musics);
    void OnExceptionOccurred(Object ref, String exceptionName);
}
