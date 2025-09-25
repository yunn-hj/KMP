//
//  HourForecast.swift
//  iosApp
//
//  Created by openobject2 on 9/19/25.
//

import SwiftUI
import Shared

struct HourForecastContent: View {
    let hourForecasts: [HourForecastUiModel]

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack {
                ForEach(hourForecasts, id: \.self) { forecast in
                    HourForecastItem(forecast: forecast)
                }
            }
            .padding(.horizontal, 12)
        }
        .background(
            RoundedRectangle(cornerRadius: 15).fill(Color(red: 0.52, green: 0.52, blue: 0.52, opacity: 0.52))
        )
        .padding(.horizontal, 16)
        .fixedSize(horizontal: false, vertical: true)
    }
}

struct HourForecastItem: View {
    let forecast: HourForecastUiModel
    
    var body: some View {
        VStack(alignment: .center) {
            Text(forecast.time)
                .foregroundStyle(.white)
                .font(.system(size: 14))
                .fontWeight(.semibold)
            
            AsyncImage(url: URL(string: forecast.iconUrl)) { image in
                image.image?.resizable().scaledToFit()
            }
            .frame(width: 50, height: 50)
            
            getUnitAttributedText(prev: forecast.temp, unitSize: 12)
                .foregroundStyle(.white)
                .font(.system(size: 14))
                .fontWeight(.semibold)
        }
        .padding(.vertical)
    }
}
