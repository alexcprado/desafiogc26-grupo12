import type { CotaPatrocinio } from "@/shared/api/enums";
import { rotuloCota } from "@/shared/lib/format";
import { Badge } from "@/shared/ui/primitives";

const variantes: Record<CotaPatrocinio, "brand" | "highlight" | "outline"> = {
  MASTER: "brand",
  SUBCOTA_G: "highlight",
  SUBCOTA: "outline",
};

export function CotaBadge({ cota }: { cota: CotaPatrocinio }) {
  return <Badge variante={variantes[cota]}>{rotuloCota[cota]}</Badge>;
}
