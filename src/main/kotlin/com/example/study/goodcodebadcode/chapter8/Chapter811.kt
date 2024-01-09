package com.example.study.goodcodebadcode.chapter8

class Chapter811 {
    class RoutePlanner1(
        private val roadMap: RoadMap = SoulRoadMap(),
    ) {
        fun planRoute(startPoint: String, endPoint: String): Pair<List<Road>, List<Junction>> {
            val roads = roadMap.getRoads()
            val junctions = roadMap.getJunctions()

            return Pair(roads, junctions)
        }
    }

    class RoutePlanner2(
        private val roadMap: RoadMap = BusanRoadMap(USE_ONLINE_MAP, INCLUDE_SEASONAL_ROADS),
    ) {
        fun planRoute(startPoint: String, endPoint: String): Pair<List<Road>, List<Junction>> {
            val roads = roadMap.getRoads()
            val junctions = roadMap.getJunctions()

            return Pair(roads, junctions)
        }

        companion object {
            const val USE_ONLINE_MAP: Boolean = true
            const val INCLUDE_SEASONAL_ROADS = false
        }
    }

    interface RoadMap {
        fun getRoads(): List<Road>
        fun getJunctions(): List<Junction>
    }

    class SoulRoadMap : RoadMap {
        override fun getRoads(): List<Road> {
            return listOf(Road("강변대로"), Road("올림픽대로"))
        }

        override fun getJunctions(): List<Junction> {
            return listOf(Junction("양재IC"), Junction("상일IC"))
        }
    }

    class BusanRoadMap(
        private val useOnlineVersion: Boolean,
        private val includeSeasonalRoads: Boolean,
    ) : RoadMap {
        override fun getRoads(): List<Road> {
            return listOf(Road("부산대로"))
        }

        override fun getJunctions(): List<Junction> {
            return listOf(Junction("부산IC"))
        }
    }

    data class Road(val name: String)
    data class Junction(val name: String)
}
