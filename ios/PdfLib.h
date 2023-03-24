
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNPdfLibSpec.h"

@interface PdfLib : NSObject <NativePdfLibSpec>
#else
#import <React/RCTBridgeModule.h>

@interface PdfLib : NSObject <RCTBridgeModule>
#endif

@end
