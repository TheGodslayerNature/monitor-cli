package com.fate.monitor_cli.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SystemMonitorTest {

    public static final int CONVERSOR_BITES_TO_GB = 1_073_741_824;
    @Mock
    private SystemInfo mockSystemInfo;
    @Mock
    private GlobalMemory mockMemory;
    @Mock
    private HardwareAbstractionLayer mockHardware;
    @Mock
    private CentralProcessor mockProcessor;
    @Mock
    private HWDiskStore diskStore;

    private SystemMonitor systemMonitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockSystemInfo.getHardware()).thenReturn(mockHardware);
        when(mockSystemInfo.getHardware().getProcessor()).thenReturn(mockProcessor);
        when(mockHardware.getMemory()).thenReturn(mockMemory);
        systemMonitor = new SystemMonitor(mockSystemInfo);
    }

    @Test
    void testGetCpuUsage() {
        long[] mockTicks = {1000, 2000, 3000, 4000};
        when(mockProcessor.getSystemCpuLoadTicks()).thenReturn(mockTicks);

        when(mockProcessor.getSystemCpuLoadBetweenTicks(any(long[].class)))
                .thenReturn(0.25);

        double cpuUsage = systemMonitor.getCpuUsage();

        assertEquals(25.0, cpuUsage, 0.01);
    }

    @Test
    void testGetMemoryAvailable() {
        when(mockMemory.getAvailable()).thenReturn(2L * CONVERSOR_BITES_TO_GB);
        when(mockMemory.getTotal()).thenReturn(8L * CONVERSOR_BITES_TO_GB);

        double availableMemory = systemMonitor.getTotalMemoryAvailable();

        assertEquals(2.0, availableMemory, 0.01);

        double usedMemory = systemMonitor.getMemoryUsed();

        assertEquals(6.0, usedMemory, 0.01);
    }

    @Test
    void testMemoryUsagePercent() {
        when(mockMemory.getAvailable()).thenReturn(5L * CONVERSOR_BITES_TO_GB);
        when(mockMemory.getTotal()).thenReturn(10L * CONVERSOR_BITES_TO_GB);

        double availableMemory = systemMonitor.getTotalMemoryAvailable();

        assertEquals(5.0, availableMemory, 0.01);

        double usedMemory = systemMonitor.getMemoryUsed();

        assertEquals(5.0, usedMemory, 0.01);

        double memoryUsagePercent = systemMonitor.getMemoryUsagePercent();

        assertEquals(50.0, memoryUsagePercent);
    }

    @Test
    void testAvailableDiskSize() {
        when(diskStore.getName()).thenReturn("Disk Local (c)");
        when(diskStore.getSize()).thenReturn(515_396_075_520L);
        when(diskStore.getModel()).thenReturn("Sean Kingston");
        when(diskStore.getReadBytes()).thenReturn(268_435_456_000L);

        List<HWDiskStore> disks = new ArrayList<>();
        disks.add(diskStore);

        when(mockHardware.getDiskStores()).thenReturn(disks);

        String info = systemMonitor.disksInfo();

        assertEquals("Disk Name: Disk Local (c);\nDisk total Size: 480,00;\nDisk Model: Sean Kingston,\nAvailable Size: 250,00",info);
    }

    @Test
    void noDisks() {
        when(mockHardware.getDiskStores()).thenReturn(Collections.emptyList());

        String info = systemMonitor.disksInfo();

        assertEquals("No disks available", info);
    }

}