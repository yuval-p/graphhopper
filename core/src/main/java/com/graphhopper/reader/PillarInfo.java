/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.reader;

import com.graphhopper.storage.DataAccess;
import com.graphhopper.storage.Directory;
import com.graphhopper.util.Helper;
import com.graphhopper.util.PointAccess;

/**
 * This class helps to store lat,lon,ele for every node parsed in OSMReader
 * <p>
 *
 * @author Peter Karich
 */
public class PillarInfo implements PointAccess {
    private final boolean enabled3D;
    private final boolean storeOSMId;
    private static int OSM_ID_BYTE_SIZE = 5;
//    private int LAT = 0 * 4, LON = 1 * 4, ELE = LON + (enabled3D? 4: 0), OSM = 2*4
    private final int LAT, LON, ELE, OSM;
    private final DataAccess da;
    private final int rowSizeInBytes;
    private final Directory dir;

    public PillarInfo(boolean enabled3D, boolean storeOSMId, Directory dir) {
        this.enabled3D = enabled3D;
        this.storeOSMId = storeOSMId;
        LAT = 0;
        LON = 4;
        ELE = LON + (enabled3D? 4: 0);
        OSM = ELE + 4;
        this.dir = dir;
        this.da = dir.create("tmp_pillar_info").create(100);
        this.rowSizeInBytes = (getDimension()) * 4 + (storeOSMId? OSM_ID_BYTE_SIZE - 4: 0);
    }

    @Override
    public boolean is3D() {
        return enabled3D;
    }

    @Override
    public boolean isStoringOSMIds() {
        return storeOSMId;
    }

    @Override
    public int getDimension() {
        int baseDimension = 2;
        if(enabled3D)
            baseDimension ++;
        if(storeOSMId)
            baseDimension ++;
        return baseDimension; //TODO
    }

    @Override
    public void ensureNode(int nodeId) {
        long tmp = (long) nodeId * rowSizeInBytes;
        da.ensureCapacity(tmp + rowSizeInBytes);
    }

    @Override
    public void setNode(int nodeId, double lat, double lon, double ele, long osmId) {
        ensureNode(nodeId);
        long tmp = (long) nodeId * rowSizeInBytes;
        da.setInt(tmp + LAT, Helper.degreeToInt(lat));
        da.setInt(tmp + LON, Helper.degreeToInt(lon));


        if(isStoringOSMIds())
            da.setBytes(tmp + OSM, Helper.longToBytes(osmId, OSM_ID_BYTE_SIZE), OSM_ID_BYTE_SIZE);

        if (is3D())
            da.setInt(tmp + ELE, Helper.eleToInt(ele));
    }

    @Override
    public double getLat(int id) {
        int intVal = da.getInt((long) id * rowSizeInBytes + LAT);
        return Helper.intToDegree(intVal);
    }

    @Override
    public double getLon(int id) {
        int intVal = da.getInt((long) id * rowSizeInBytes + LON);
        return Helper.intToDegree(intVal);
    }

    @Override
    public double getEle(int id) {
        if (!is3D())
            return Double.NaN;

        int intVal = da.getInt((long) id * rowSizeInBytes + ELE);
        return Helper.intToEle(intVal);
    }

    @Override
    public long getOsmId(int id) {
        if(!isStoringOSMIds())
            return Long.MIN_VALUE;
        byte[] value = new byte[OSM_ID_BYTE_SIZE];
        da.getBytes((long) id * rowSizeInBytes + OSM, value, OSM_ID_BYTE_SIZE);
        return Helper.bytesToLong(value);
    }

    public void clear() {
        dir.remove(da.getName());
    }
}
