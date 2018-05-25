//
//  MD5Encrypt.h
//  RNThumbnail
//
//  Created by Nevo on 2018/5/25.
//  Copyright © 2018 Facebook. All rights reserved.
//

#ifndef MD5Encrypt_h
#define MD5Encrypt_h
#import <AVFoundation/AVFoundation.h>

@interface MD5Encrypt : NSObject
// MD5加密
/*
 *由于MD5加密是不可逆的,多用来进行验证
 */
// 32位小写
+(NSString *)MD5ForLower32Bate:(NSString *)str;
// 32位大写
+(NSString *)MD5ForUpper32Bate:(NSString *)str;
// 16为大写
+(NSString *)MD5ForUpper16Bate:(NSString *)str;
// 16位小写
+(NSString *)MD5ForLower16Bate:(NSString *)str;
@end

#endif /* MD5Encrypt_h */
