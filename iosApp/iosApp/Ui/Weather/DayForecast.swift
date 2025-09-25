//
//  DayForecast.swift
//  iosApp
//
//  Created by openobject2 on 9/19/25.
//

import SwiftUI
import Shared

struct DayForecastContent: View {
    let dayForecasts: [DayForecastUiModel]
    
    init(dayForecasts: [DayForecastUiModel]) {
        self.dayForecasts = dayForecasts
    }
    
    var body: some View {
        VStack(spacing: 12) {
            ForEach(dayForecasts, id: \.dayOfWeek) { forecast in
                DayForecastItem(forecast: forecast)
            }
        }
        .padding(20)
        .background(
            RoundedRectangle(cornerRadius: 15).fill(Color(red: 0.52, green: 0.52, blue: 0.52, opacity: 0.52))
        )
        .padding(.horizontal, 16)
        .fixedSize(horizontal: false, vertical: true)
    }
}


struct DayForecastItem: View {
    let forecast: DayForecastUiModel
    private let dayOfWeekColor: Color
    private let dayOfWeekWeight: Font.Weight
    
    init(forecast: DayForecastUiModel) {
        self.forecast = forecast
        
        dayOfWeekColor = switch forecast.dayOfWeek {
        case "일": .red
        case "토": .blue
        default: .white
        }
        dayOfWeekWeight = if forecast.dayOfWeek == "오늘" {
            Font.Weight.bold
        } else {
            Font.Weight.medium
        }
    }
    
    var body: some View {
        HStack {
            Text(forecast.dayOfWeek).foregroundStyle(dayOfWeekColor).fontWeight(dayOfWeekWeight)
            Spacer()
            AsyncImage(url: URL(string: forecast.iconUrl)) { image in
                image.image?.resizable().scaledToFit()
            }
            .frame(width: 50, height: 50)
            .padding(.trailing, 20)
            getUnitAttributedText(prev: "\(forecast.maxTemp)  /  \(forecast.minTemp)", unitSize: 12)
                .foregroundColor(.white)
                .fontWeight(dayOfWeekWeight)
        }
    }
}
