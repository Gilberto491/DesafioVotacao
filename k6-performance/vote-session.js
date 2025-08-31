// vote-session-once.js (trecho principal)
import http from 'k6/http';
import { check } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';

export const options = {
  scenarios: {
    voters_once: {
      executor: 'per-vu-iterations',
      vus: Number(__ENV.VOTERS || 100), 
      iterations: 1,                    
      maxDuration: __ENV.MAX_DURATION || '1m',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<800', 'p(99)<1500'],
  },
};

export function setup() {
  const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
  const sessionId = __ENV.SESSION_ID;
  if (!sessionId) throw new Error('Passe -e SESSION_ID=<id_da_sessao>');
  return { baseUrl: BASE_URL, sessionId };
}

function cpfFromVu(vu) {
  const base = String(100000000 + vu).slice(-9).split('').map(Number);
  let s = 0; for (let i=0;i<9;i++) s += base[i]*(10-i);
  let dv1 = 11 - (s%11); if (dv1 >= 10) dv1 = 0;
  s = 0; for (let i=0;i<9;i++) s += base[i]*(11-i); s += dv1*2;
  let dv2 = 11 - (s%11); if (dv2 >= 10) dv2 = 0;
  return base.join('') + dv1 + dv2;
}

export default function (data) {
  const cpf = cpfFromVu(__VU);             
  const choice = Math.random() < 0.5 ? 'SIM' : 'NAO';

  const res = http.post(
    `${data.baseUrl}/api/v1/sessions/${data.sessionId}/votes`,
    JSON.stringify({ cpf, choice }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  check(res, {
  'status válido': (r) => [200, 201, 422].includes(r.status),
  });
}

export function handleSummary(d) {
  return { 'report.html': htmlReport(d), 'summary.json': JSON.stringify(d, null, 2) };
}
