const { fetch } = require('undici');
const fs = require('fs');

const TARGET_URL = 'http://localhost:8080/chat/webflux-stream';
const TOTAL_REQUESTS = 10000;
const NUM_RUNS = 10;
const PAUSE_BETWEEN_RUNS_MS = 15000;
const BATCH_SIZE = 500;
const PAUSE_BETWEEN_BATCHES_MS = 1000;

async function runSingleTest(index, results) {
    const start = new Date();
    let ttfb = null;

    try {
        const res = await fetch(TARGET_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ param: "Test" })
        });

        const reader = res.body?.getReader?.();
        if (!reader) throw new Error('Stream not supported or reader missing');

        const decoder = new TextDecoder();
        let total = '';

        while (true) {
            const { value, done } = await reader.read();
            if (done) break;

            const now = new Date();
            if (!ttfb) {
                ttfb = now - start;
            }

            const chunk = decoder.decode(value);
            total += chunk;

            if (chunk.includes('"stop"') || chunk.includes('"finish_reason":"stop"')) {
                break;
            }
        }

        const end = new Date();
        const duration = end - start;

        results.push({
            requestIndex: index + 1,
            startTime: start.toISOString(),
            endTime: end.toISOString(),
            duration,
            ttfb: ttfb ?? duration,
            status: res.status,
            success: res.ok,
            error: ''
        });

        console.log(`Request ${index + 1} done in ${duration} ms (TTFB: ${ttfb} ms)`);
    } catch (err) {
        const end = new Date();
        const duration = end - start;

        results.push({
            requestIndex: index + 1,
            startTime: start.toISOString(),
            endTime: end.toISOString(),
            duration,
            ttfb: 0,
            status: 0,
            success: false,
            error: err.message
        });

        console.error(`Request ${index + 1} failed: ${err.message}`);
    }
}

async function runTestRound(roundNumber) {
    console.log(`\nStarte Testrunde ${roundNumber}...\n`);
    const results = [];

    for (let i = 0; i < TOTAL_REQUESTS; i += BATCH_SIZE) {
        const batch = Array.from({ length: BATCH_SIZE }, (_, j) =>
            runSingleTest(i + j, results)
        );

        console.log(`Sende Batch ${i / BATCH_SIZE + 1} mit ${BATCH_SIZE} Requests...`);
        await Promise.all(batch);

        if (i + BATCH_SIZE < TOTAL_REQUESTS) {
            console.log(`Warte ${PAUSE_BETWEEN_BATCHES_MS}ms vor nächstem Batch...`);
            await new Promise(r => setTimeout(r, PAUSE_BETWEEN_BATCHES_MS));
        }
    }

    const csvHeader = 'requestIndex,startTime,endTime,duration,ttfb,status,success,error\n';
    const csvRows = results.map(r =>
        `${r.requestIndex},${r.startTime},${r.endTime},${r.duration},${r.ttfb},${r.status},${r.success},${r.error.replace(/[\n\r,]/g, ' ')}`
    ).join('\n');

    const filename = `results_webflux_run_${roundNumber}.csv`;
    fs.writeFileSync(filename, csvHeader + csvRows);
    console.log(`Ergebnisse von Runde ${roundNumber} gespeichert in ${filename}`);
}

(async () => {
    for (let run = 1; run <= NUM_RUNS; run++) {
        await runTestRound(run);

        if (run < NUM_RUNS) {
            console.log(`Warte ${PAUSE_BETWEEN_RUNS_MS / 1000} Sekunden bis zur nächsten Runde...`);
            await new Promise(r => setTimeout(r, PAUSE_BETWEEN_RUNS_MS));
        }
    }

    console.log('\nAlle Testrunden abgeschlossen.');
})();