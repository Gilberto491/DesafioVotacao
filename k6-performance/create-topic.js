import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';

export const options = {
  vus: 100,
  duration: '30s',
};

export function handleSummary(data) {
  return {
    "report.html": htmlReport(data),
  };
}

export default function () {
  const url = 'http://localhost:8080/api/v1/topics';
  const uniqueTitle = `Assembleia-${__ITER}-${__VU}`;
  const payload = JSON.stringify({
    title: uniqueTitle,
    description: 'Pauta de votação',
  });

  const headers = { 'Content-Type': 'application/json' };

  const res = http.post(url, payload, { headers });
  check(res, { 'status é 201': (r) => r.status === 201 });

  sleep(1);
}
