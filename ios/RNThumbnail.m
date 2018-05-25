
#import "RNThumbnail.h"
#import <AVFoundation/AVFoundation.h>
#import <AVFoundation/AVAsset.h>
#import <UIKit/UIKit.h>
#import "MD5Encrypt.h"

@implementation RNThumbnail

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(get:(NSString *)filepath resolve:(RCTPromiseResolveBlock)resolve
                               reject:(RCTPromiseRejectBlock)reject)
{
    @try {
        filepath = [filepath stringByReplacingOccurrencesOfString:@"file://"
                                                  withString:@""];
        NSURL *vidURL = [NSURL fileURLWithPath:filepath];
        
        NSString *md5 = [MD5Encrypt MD5ForLower32Bate: [vidURL absoluteString]];
        
        NSString* tempDirectory = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory,
                                                                       NSUserDomainMask,
                                                                       YES) lastObject];
        
        NSString *fullPath = [tempDirectory stringByAppendingPathComponent: [NSString stringWithFormat:@"thumb-%@.jpg", md5]];
        
        NSFileManager *fileManager = [NSFileManager defaultManager];
        BOOL result = [fileManager fileExistsAtPath:fullPath];
        
        if (result) {
            if (resolve)
                resolve(@{ @"path" : fullPath });
            return;
        }
        
        AVURLAsset *asset = [[AVURLAsset alloc] initWithURL:vidURL options:nil];
        AVAssetImageGenerator *generator = [[AVAssetImageGenerator alloc] initWithAsset:asset];
        generator.appliesPreferredTrackTransform = YES;
        
        NSError *err = NULL;
        CMTime time = CMTimeMake(1, 60);
        
        CGImageRef imgRef = [generator copyCGImageAtTime:time actualTime:NULL error:&err];
        UIImage *thumbnail = [UIImage imageWithCGImage:imgRef];
        
        if (thumbnail) {
            // save to temp directory
            NSData *data = UIImageJPEGRepresentation(thumbnail, 0.6);
            NSFileManager *fileManager = [NSFileManager defaultManager];
        
            [fileManager createFileAtPath:fullPath contents:data attributes:nil];
            if (resolve)
                resolve(@{ @"path" : fullPath,
                           @"width" : [NSNumber numberWithFloat: thumbnail.size.width],
                           @"height" : [NSNumber numberWithFloat: thumbnail.size.height] });
        } else {
            if (resolve) {
                resolve(@{});
            }
        }
    } @catch(NSException *e) {
        reject(e.reason, nil, nil);
    }
}

@end
  
