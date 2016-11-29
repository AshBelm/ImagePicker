package com.mcmo.z.imagepicker.library.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


/**
 * Created by weizhang210142 on 2016/2/4.
 */
public class ImageLoaderUtil {
    public static void initImageLoaderConfing(Context context){
//        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
//                .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
//                .taskExecutor(...)
//                 .taskExecutorForCachedImages(...)
                .threadPoolSize(3) // default  线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY) // default 设置当前线程的优先级
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LRULimitedMemoryCache(10 * 1024 * 1024)) //可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
//                .memoryCacheSizePercentage(13) // default
//                .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
//                .diskCacheFileCount(100)  // 可以缓存的文件数量
                        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                  .imageDownloader(new HttpClientImageDownloader(context))//default BaseImageDownloader(context)
//                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(getDisplayOptions()) // default 设置默认的显示操作，当在display方法中没有传递option时会被使用
//                .writeDebugLogs() // 打印debug log
                .build(); //开始构建
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getDisplayOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
                .cacheOnDisk(false) // default  设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY) // default 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
                .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                .build();
        return options;
    }
    public static DisplayImageOptions getDisplayOptions_ImgOriginal(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.default_img) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.default_img) // 设置图片加载或解码过程中发生错误显示的图片
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY) // default 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
                .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                .build();
        return options;
    }
//    public static void getDisplayOptions(){
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_img) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.default_img) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.default_img) // 设置图片加载或解码过程中发生错误显示的图片
//                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
//                .delayBeforeLoading(1000)  // 下载前的延迟时间
//                .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
//                .decodingOptions(...)  // 图片的解码设置
//                .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
//                .handler(new Handler()) // default\
//                .build();
//    }

//    1）.imageScaleType(ImageScaleType imageScaleType)  //设置图片的缩放方式
//    缩放类型mageScaleType:
//    EXACTLY :图像将完全按比例缩小的目标大小
//    EXACTLY_STRETCHED:图片会缩放到目标大小完全
//    IN_SAMPLE_INT:图像将被二次采样的整数倍
//    IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//    NONE:图片不会调整
//    2）.displayer(BitmapDisplayer displayer)   //设置图片的显示方式
//    显示方式displayer：
//    RoundedBitmapDisplayer（int roundPixels）设置圆角图片
//    FakeBitmapDisplayer（）这个类什么都没做
//    FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
//    SimpleBitmapDisplayer()正常显示一张图片

//    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//    .displayer(new FadeInBitmapDisplayer(100))// 图片加载好后渐入的动画时间
}
