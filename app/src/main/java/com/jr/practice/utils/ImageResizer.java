package com.jr.practice.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 高效的加载Bitmap,采用BitmapFactory.Options来加载所需尺寸的图片。
 * 这里假设通过ImageView来显示图片，很多时候ImageView并没有图片的原始尺寸那么大，
 * 这个时候把整个图片加载进来后再设给ImageView，这显然是没有必要的，因为ImageView
 * 并没有办法显示原始的图片。通过BitmapFactory.Options就可以按一定的采样率来加载缩小后
 * 的图片，将缩小后的图片在ImageView中显示，这样就会降低内存占从而在一定程度上避免OOM，
 * 提高了Bitmap加载时的性能。BitmapFactory提供的加载图片的四类方法都支持BitmapFactory.Option参数
 * 通过他可以很方便的对一个图片进行缩放采样
 * Created by Administrator on 2016-08-17.
 */

public class ImageResizer {
    public ImageResizer() {
    }

    /**
     * 通过BitmapFactory.Options来缩放图片，主要用到了它的inSimpleSize参数，即采样率。
     * 当inSampleSize设置为1时，采样后的图片大小为图片的原始大小；当inSampleSize大于1时，
     * 比如为2，那么采样后的图片的其宽高均为原始图片的1/2，而像素数为原图的1/4，其占有的
     * 内存大小也为原图的1/4。
     * 官方文档指出，inSampleSize的取值应该总是2的指数，即2,4,8,16 etc.
     * 如果不是2的指数，系统会向下取整
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSimpledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //Calculate inSampleSize/*控制缩放*/
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSample set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap decodeSimpleBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        //First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        //Calculate inSampleSize    /*采样率*/
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


    /**
     * 计算图片采样率
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;   /*原始大小*/
        }
        //Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        L.d("origin,w = " + width + ",h = " + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //Calculate the largest inSampleSize value that is a power of 2 and
            //keeps both height and width larger than the requested and width
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        L.d("inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

}
