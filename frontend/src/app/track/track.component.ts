import {Component, OnDestroy, OnInit} from '@angular/core';
import {icon, latLng, Map, marker, point, polyline, tileLayer} from 'leaflet';
import {TrackService} from './track.service';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {AuthService} from '../core/service/auth.service';
import * as L from 'leaflet';

@Component({
  selector: 'track',
  templateUrl: './track.component.html',
  styleUrls: ['./track.component.scss']
})
export class TrackComponent implements OnInit, OnDestroy {

  options = {
    layers: [
      tileLayer('https://{s}.tile.osm.org/{z}/{x}/{y}.png', {maxZoom: 18, attribution: '...'})
    ],
    zoom: 9,
    zoomControl: false,
    center: latLng(47.497912, 19.040235)
  };
  layer = null;
  vehicleMarker = null;
  map: Map = null;
  stompClient = null;
  trackSubscription: any = null;
  trackStatsSubscription: any = null;
  selectedTrackId: string = null;
  selectedRow: any;

  constructor(private trackService: TrackService, private authService: AuthService) {
    const ws = new SockJS('http://localhost:8090/socket?access_token=' + authService.getToken().token);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({'Authorization': 'Bearer ' + authService.getToken().token}, null);
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.clearMap();
    this.unsubscribeTrackSocket();
    this.unsubscribeTrackStatsSocket();
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }

  onMapReady(map: Map) {
    this.map = map;
    L.control.zoom({
      position: 'topright'
    }).addTo(map);
  }

  rowClickHandler(event: any): void {
    this.clearMap();
    this.unsubscribeTrackSocket();
    this.unsubscribeTrackStatsSocket();
    this.selectedTrackId = event.trackId;
    this.trackService.getPath(event.trackId).subscribe(value => {
      const route = this.drawRouteOnMap(value, event.trackStatus === 'STARTED');
      this.map.flyToBounds(route.getBounds(), {
        padding: point(100, 100),
        maxZoom: 12,
        animate: true
      });
      this.subscribeTrackSocket(event.trackId);
      this.subscribeTrackStatsSocket(event.trackId);
    });
  }

  convertMultiPointToPolyLine(multiPoint: any, dashed: boolean): any {
    const coordinates = [];
    for (const item of multiPoint) {
      coordinates.push([item.latitude, item.longitude]);
    }
    return polyline(coordinates, dashed ? {
      opacity: 1,
      dashArray: '5,5',
    } : {});
  }

  drawRouteOnMap(multiPoint: any, dashed: boolean): any {
    this.layer = this.convertMultiPointToPolyLine(multiPoint, dashed);
    return this.layer;
  }

  clearRouteOnMap(): void {
    this.layer = null;
  }

  drawMarkerOnMap(latitude: number, longitude: number, type = 'start'): any {
    this.vehicleMarker = marker([latitude, longitude], {
      icon: icon({
        iconUrl: '/assets/marker-icon-' + type + '.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
      })
    });
  }

  clearMarkerOnMap(): void {
    this.vehicleMarker = null;
  }

  clearMap(): void {
    this.clearRouteOnMap();
    this.clearMarkerOnMap();
  }

  subscribeTrackSocket(trackId): void {
    this.trackSubscription = this.stompClient.subscribe('/topic/track/' + trackId, (message) => {
      const multiPoint = JSON.parse(message.body).multiPoint[0];
      this.drawMarkerOnMap(multiPoint.latitude, multiPoint.longitude, 'vehicle');
    });
  }

  unsubscribeTrackSocket(): void {
    if (this.trackSubscription) {
      this.stompClient.unsubscribe(this.trackSubscription.id, {'destination': '/topic/track/' + this.selectedTrackId});
    }
  }

  subscribeTrackStatsSocket(trackId): void {
    this.trackStatsSubscription = this.stompClient.subscribe('/topic/track/stats/' + trackId, (message) => {
      if (message && message.body) {
        const stats = JSON.parse(message.body);
        this.selectedRow.duration = stats.duration;
        this.selectedRow.averageSpeed = stats.averageSpeed;
        this.selectedRow.distance = stats.distance;
      }
    });
  }

  unsubscribeTrackStatsSocket(): void {
    if (this.trackStatsSubscription) {
      this.stompClient.unsubscribe(this.trackStatsSubscription.id, {'destination': '/topic/track/stats/' + this.selectedTrackId});
    }
  }


}
