#import "YoutubeFetch.h"
#import <React/RCTBridgeModule.h>

@interface YoutubeFetch() <RCTBridgeModule>
@end

@implementation YoutubeFetch

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(fetchData:(NSString *)urlString
                  headers:(NSDictionary *)headers
                  body:(NSString *)body
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  NSURL *url = [NSURL URLWithString:urlString];
  NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
  [request setHTTPMethod:@"POST"];
  
  // Set headers
  for (NSString *key in headers) {
    [request setValue:headers[key] forHTTPHeaderField:key];
  }
  
  [request setHTTPBody:[body dataUsingEncoding:NSUTF8StringEncoding]];
  
  NSURLSessionDataTask *task = [[NSURLSession sharedSession] dataTaskWithRequest:request
    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
      if (error) {
        reject(@"ERROR", @"Request failed", error);
      } else {
        NSString *responseString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        resolve(responseString);
      }
    }];
  [task resume];
}

@end
