import type { StatusPatrocinador } from "@/shared/api/enums";
import { rotuloStatus } from "@/shared/lib/format";
import { Badge } from "@/shared/ui/primitives";

export function StatusPatrocinadorBadge({ status }: { status: StatusPatrocinador }) {
  return (
    <Badge variante={status === "ATIVO" ? "success" : "neutral"}>
      <span
        className={
          status === "ATIVO"
            ? "size-1.5 rounded-full bg-success"
            : "size-1.5 rounded-full bg-muted-foreground"
        }
      />
      {rotuloStatus[status]}
    </Badge>
  );
}
