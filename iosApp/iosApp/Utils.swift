//
//  Utils.swift
//  iosApp
//
//  Created by openobject2 on 9/11/25.
//

import Shared
import SwiftUI

private var notifiedTodo = false

func notifyTodo(todoList: [TodoWithCategory]) {
    if notifiedTodo { return }

    let notificationCenter = UNUserNotificationCenter.current()
    let authOptions: UNAuthorizationOptions = [.alert, .sound, .badge]

    notificationCenter.requestAuthorization(options: authOptions) {
        success,
        error in
        if let error = error {
            print(error)
            return
        }
        guard success else {
            print("알림 권한 거부됨")
            return
        }
    }

    let finished = todoList.filter { content in
        let todo = content.todo
        return todo.isCompleted == false
            && todo.dueMs <= Int64(Date().timeIntervalSince1970 * 1000)
    }
    dump("yhj >> \(finished)")
    if finished.isEmpty { return }

    let content = UNMutableNotificationContent()

    content.title = "미완료 할일 알림"
    content.body = finished.map { content in
        let todo = content.todo
        return if todo.content.isEmpty { "이름없음" } else { todo.content }
    }.joined(separator: ", ")
    content.sound = .default

    let trigger = UNTimeIntervalNotificationTrigger(
        timeInterval: 1,
        repeats: false
    )
    let request = UNNotificationRequest(
        identifier: "notify_todo",
        content: content,
        trigger: trigger
    )

    notificationCenter.add(request)
    notifiedTodo = true
}

func getColor(colorName: String) -> Color {
    return switch colorName {
    case "red": Color.red
    case "yellow": Color.yellow
    case "green": Color.green
    case "blue": Color.blue
    case "gray": Color.gray
    default:
        Color.gray
    }
}

func getUnitAttributedText(prev: String, unitSize: CGFloat) -> Text {
    var combinedText = Text("")
    let pattern = "([0-9.]+|[^0-9.]+)"

    do {
        let regex = try NSRegularExpression(pattern: pattern)
        let range = NSRange(prev.startIndex..., in: prev)

        regex.matches(in: prev, options: [], range: range).forEach { match in
            guard let partRange = Range(match.range, in: prev) else { return }
            let part = String(prev[partRange])

            if part.allSatisfy({ $0.isNumber || $0 == "." }) {
                combinedText = combinedText + Text(part)
            } else {
                combinedText =
                    combinedText
                    + Text(part)
                    .font(.system(size: unitSize))
                    .fontWeight(.light)
            }
        }
    } catch {
        print("\(error.localizedDescription)")
        return Text(prev)
    }

    return combinedText
}

func getForecastAnimationName(forecast: HourForecast?) -> String? {
    guard let forecast = forecast, let weatherInfo = forecast.weather.first
    else {
        return nil
    }

    let cur = Calendar.current.component(.hour, from: Date())
    let sunset = Calendar.current.component(
        .hour,
        from: Date.init(timeIntervalSince1970: TimeInterval(forecast.dt))
    )
    let isMorning = cur <= sunset
    let condition = weatherInfo.main.lowercased()

    return if condition.contains("sun") || condition.contains("clear") {
        isMorning ? "anim_sunny_morning" : "anim_sunny_night"
    } else if condition.contains("cloud") {
        "anim_cloudy"
    } else if condition.contains("rain") {
        isMorning ? "anim_rainy_morning" : "anim_rainy_night"
    } else if condition.contains("snow") {
        "anim_snowy"
    } else if condition.contains("thunder") {
        "anim_thunder"
    } else {
        nil
    }
}
