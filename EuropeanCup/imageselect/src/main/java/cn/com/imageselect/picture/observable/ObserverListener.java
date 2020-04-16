package cn.com.imageselect.picture.observable;




import java.util.List;

import cn.com.imageselect.picture.entity.LocalMedia;
import cn.com.imageselect.picture.entity.LocalMediaFolder;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.observable
 * email：893855882@qq.com
 * data：17/1/16
 */
public interface ObserverListener {
    void observerUpFoldersData(List<LocalMediaFolder> folders);

    void observerUpSelectsData(List<LocalMedia> selectMedias);
}
