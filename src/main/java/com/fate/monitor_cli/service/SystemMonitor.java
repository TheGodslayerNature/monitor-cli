package com.fate.monitor_cli.service;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@Service
public class SystemMonitor {

    private final SystemInfo systemInfo;
    private long[] prevTicks;

    public SystemMonitor(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
        this.prevTicks = systemInfo.getHardware().getProcessor().getSystemCpuLoadTicks();
    }

    public double getCpuUsage() {
        CentralProcessor centralProcessor = systemInfo.getHardware().getProcessor();
        long[] ticks = centralProcessor.getSystemCpuLoadTicks();
        double cpu = centralProcessor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        this.prevTicks = ticks;
        return cpu;
    }
}
