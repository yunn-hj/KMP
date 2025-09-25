import SwiftUI
import Shared
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
    // 앱 처음 실행 시 호출
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        
        UNUserNotificationCenter.current().delegate = self
        
        return true
    }
    
    // 앱이 포그라운드에 있을 때 알림이 오면 호출
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        // 배너와 리스트 형태로 알림을 표시하도록 설정
        completionHandler([.banner, .list, .sound])
    }
}

@main
struct iOSApp: App {
    // AppDelegate를 시스템에 연결
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinHelperKt.doInitKoin { _ in }
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
